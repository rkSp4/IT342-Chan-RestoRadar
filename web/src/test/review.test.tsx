import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { MemoryRouter } from 'react-router'
import { AddReviewPage } from '../features/review/AddReviewPage'
import { AuthProvider } from '../features/auth/AuthContext'

const mockFetch = vi.fn()
global.fetch = mockFetch

vi.mock('react-router', async () => {
  const actual = await vi.importActual('react-router')
  return {
    ...actual,
    useNavigate: () => vi.fn(),
    useParams: () => ({ id: '1' }),
  }
})

beforeEach(() => {
  mockFetch.mockClear()
  localStorage.setItem('restoradar_access_token', 'mock-token')
})

const renderWithProviders = (component: React.ReactNode) => {
  return render(
    <MemoryRouter>
      <AuthProvider>
        {component}
      </AuthProvider>
    </MemoryRouter>
  )
}

describe('AddReviewPage', () => {
  it('renders review form correctly', () => {
    renderWithProviders(<AddReviewPage />)
    expect(screen.getByPlaceholderText(/share your experience/i)).toBeTruthy()
    expect(screen.getByRole('button', { name: /submit/i })).toBeTruthy()
  })

  it('does not call API when comment is empty', async () => {
    renderWithProviders(<AddReviewPage />)
    fireEvent.click(screen.getByRole('button', { name: /submit/i }))
    await waitFor(() => {
      expect(mockFetch).not.toHaveBeenCalled()
    })
  })

  it('calls review API with correct data when form is valid', async () => {
    mockFetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({ success: true, reviewId: 'abc123' })
    })

    renderWithProviders(<AddReviewPage />)

    // Click the first star div (index 0 = 1 star)
    const starContainer = document.querySelector('.flex.items-center.gap-1')
    const stars = starContainer?.querySelectorAll('.relative')
    if (stars && stars.length > 0) {
      fireEvent.click(stars[0])
    }

    // Fill in comment
    fireEvent.change(screen.getByPlaceholderText(/share your experience/i), {
      target: { value: 'Great food and service!' }
    })

    fireEvent.click(screen.getByRole('button', { name: /submit/i }))

    await waitFor(() => {
      expect(mockFetch).toHaveBeenCalledWith(
        expect.stringContaining('/reviews'),
        expect.objectContaining({ method: 'POST' })
      )
    }, { timeout: 3000 })
  })
})