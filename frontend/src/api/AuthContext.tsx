import { createContext, useContext, useState, type ReactNode } from 'react'
import type { UserResponse } from './auth'

interface AuthState {
  token: string | null
  user: UserResponse | null
  setAuth: (token: string, user: UserResponse) => void
  clearAuth: () => void
}

const AuthContext = createContext<AuthState | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(null)
  const [user, setUser] = useState<UserResponse | null>(null)

  function setAuth(nextToken: string, nextUser: UserResponse) {
    setToken(nextToken)
    setUser(nextUser)
  }

  function clearAuth() {
    setToken(null)
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ token, user, setAuth, clearAuth }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) {
    throw new Error('useAuth must be used within AuthProvider')
  }
  return ctx
}
