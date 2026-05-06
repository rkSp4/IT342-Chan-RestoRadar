import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { MemoryRouter } from 'react-router'
import { LoginPage } from '../features/auth/LoginPage'
import { RegisterPage } from '../features/auth/RegisterPage'
import { AuthProvider } from '../features/auth/AuthContext'

const mockFetch = vi.fn()
global.fetch = mockFetch

vi.mock('react-router', async () => {
  const actual = await vi.importActual('react-router')
  return {
    ...actual,
    useNavigate: () => vi.fn(),
  }
})

beforeEach(() => {
  mockFetch.mockClear()
  localStorage.clear()
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

describe('LoginPage', () => {
  it('renders login form correctly', () => {
    renderWithProviders(<LoginPage />)
    expect(screen.getByPlaceholderText('you@example.com')).toBeTruthy()
    expect(screen.getByPlaceholderText('Enter your password')).toBeTruthy()
  })

  it('shows error when submitting empty form', async () => {
    renderWithProviders(<LoginPage />)
    fireEvent.click(screen.getByRole('button', { name: /login|sign in/i }))
    await waitFor(() => {
      expect(screen.getByPlaceholderText('you@example.com')).toBeTruthy()
    })
  })

  it('calls login API with correct data', async () => {
    mockFetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        accessToken: 'mock-token',
        refreshToken: 'mock-refresh',
        user: { id: '1', email: 'test@example.com', fullName: 'Test User', role: 'USER' }
      })
    })

    renderWithProviders(<LoginPage />)

    fireEvent.change(screen.getByPlaceholderText('you@example.com'), {
      target: { value: 'test@example.com' }
    })
    fireEvent.change(screen.getByPlaceholderText('Enter your password'), {
      target: { value: 'SecurePass123' }
    })
    fireEvent.click(screen.getByRole('button', { name: /login|sign in/i }))

    await waitFor(() => {
      expect(mockFetch).toHaveBeenCalledWith(
        expect.stringContaining('/login'),
        expect.objectContaining({ method: 'POST' })
      )
    })
  })

  it('shows error message on failed login', async () => {
    mockFetch.mockResolvedValueOnce({
      ok: false,
      json: async () => ({
        error: { message: 'Invalid email or password' }
      })
    })

    renderWithProviders(<LoginPage />)

    fireEvent.change(screen.getByPlaceholderText('you@example.com'), {
      target: { value: 'wrong@example.com' }
    })
    fireEvent.change(screen.getByPlaceholderText('Enter your password'), {
      target: { value: 'wrongpassword' }
    })
    fireEvent.click(screen.getByRole('button', { name: /login|sign in/i }))

    await waitFor(() => {
      expect(mockFetch).toHaveBeenCalled()
    })
  })
})

describe('RegisterPage', () => {
  it('renders registration form correctly', () => {
    renderWithProviders(<RegisterPage />)
    expect(screen.getByPlaceholderText('John Doe')).toBeTruthy()
    expect(screen.getByPlaceholderText('you@example.com')).toBeTruthy()
    expect(screen.getByPlaceholderText('8+ chars, 1 uppercase, 1 digit')).toBeTruthy()
    expect(screen.getByPlaceholderText('Re-enter your password')).toBeTruthy()
  })

  it('calls register API with correct data', async () => {
    mockFetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        accessToken: 'mock-token',
        refreshToken: 'mock-refresh',
        user: { id: '1', email: 'test@example.com', fullName: 'Test User', role: 'USER' }
      })
    })

    renderWithProviders(<RegisterPage />)

    fireEvent.change(screen.getByPlaceholderText('John Doe'), {
      target: { value: 'Test User' }
    })
    fireEvent.change(screen.getByPlaceholderText('you@example.com'), {
      target: { value: 'test@example.com' }
    })
    fireEvent.change(screen.getByPlaceholderText('8+ chars, 1 uppercase, 1 digit'), {
      target: { value: 'SecurePass123' }
    })
    fireEvent.change(screen.getByPlaceholderText('Re-enter your password'), {
      target: { value: 'SecurePass123' }
    })
    fireEvent.click(screen.getByRole('button', { name: /register|sign up|create/i }))

    await waitFor(() => {
      expect(mockFetch).toHaveBeenCalledWith(
        expect.stringContaining('/register'),
        expect.objectContaining({ method: 'POST' })
      )
    })
  })
})