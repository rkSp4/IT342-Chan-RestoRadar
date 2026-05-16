import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { MemoryRouter } from 'react-router'
import { RestaurantCard } from '../features/restaurant/RestaurantCard'

const mockRestaurant = {
  id: '1',
  name: 'Test Restaurant',
  cuisine: 'Filipino',
  priceRange: '₱₱',
  rating: 4.5,
  reviewCount: 10,
  distance: 1.2,
  address: '123 Test St',
  isOpen: true,
  tags: ['Local', 'Casual'],
  imageUrl: '',
  description: 'A test restaurant'
}

describe('RestaurantCard', () => {
  it('renders restaurant name correctly', () => {
    render(
      <MemoryRouter>
        <RestaurantCard restaurant={mockRestaurant} />
      </MemoryRouter>
    )
    expect(screen.getByText('Test Restaurant')).toBeTruthy()
  })

  it('renders cuisine type correctly', () => {
    render(
      <MemoryRouter>
        <RestaurantCard restaurant={mockRestaurant} />
      </MemoryRouter>
    )
    expect(screen.getByText('Filipino')).toBeTruthy()
  })

  it('renders price range correctly', () => {
    render(
      <MemoryRouter>
        <RestaurantCard restaurant={mockRestaurant} />
      </MemoryRouter>
    )
    expect(screen.getByText('₱₱')).toBeTruthy()
  })

  it('renders rating correctly', () => {
    render(
      <MemoryRouter>
        <RestaurantCard restaurant={mockRestaurant} />
      </MemoryRouter>
    )
    expect(screen.getByText('4.5')).toBeTruthy()
  })
})