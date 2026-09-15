export interface UserResponse {
  id: number
  email: string
  displayName: string | null
}

export interface AuthResponse {
  token: string
  user: UserResponse
}

async function handle<T>(res: Response): Promise<T> {
  if (!res.ok) {
    const body = await res.json().catch(() => ({}))
    throw new Error(body.message ?? `요청 실패 (${res.status})`)
  }
  return res.json()
}

function postJson<T>(path: string, body: unknown): Promise<T> {
  return fetch(`/api${path}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  }).then((res) => handle<T>(res))
}

export function signup(email: string, password: string): Promise<AuthResponse> {
  return postJson<AuthResponse>('/auth/signup', { email, password })
}

export function login(email: string, password: string): Promise<AuthResponse> {
  return postJson<AuthResponse>('/auth/login', { email, password })
}

export function fetchMe(token: string): Promise<UserResponse> {
  return fetch('/api/auth/me', {
    headers: { Authorization: `Bearer ${token}` },
  }).then((res) => handle<UserResponse>(res))
}
