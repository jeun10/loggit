import { useState, type FormEvent } from 'react'
import { Link, useNavigate } from 'react-router'
import { login } from '../api/auth'
import { useAuth } from '../api/AuthContext'

export function LoginPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const { setAuth } = useAuth()
  const navigate = useNavigate()

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    setError(null)
    try {
      const { token, user } = await login(email, password)
      setAuth(token, user)
      navigate('/')
    } catch (err) {
      setError(err instanceof Error ? err.message : '로그인 실패')
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <h1>로그인</h1>
      <div>
        <input
          type="email"
          placeholder="이메일"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
      </div>
      <div>
        <input
          type="password"
          placeholder="비밀번호"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
      </div>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <button type="submit">로그인</button>
      <p>
        계정이 없으신가요? <Link to="/signup">회원가입</Link>
      </p>
    </form>
  )
}
