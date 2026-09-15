import { useState, type FormEvent } from 'react'
import { Link, useNavigate } from 'react-router'
import { signup } from '../api/auth'
import { useAuth } from '../api/AuthContext'

export function SignupPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const { setAuth } = useAuth()
  const navigate = useNavigate()

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    setError(null)
    try {
      const { token, user } = await signup(email, password)
      setAuth(token, user)
      navigate('/')
    } catch (err) {
      setError(err instanceof Error ? err.message : '회원가입 실패')
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <h1>회원가입</h1>
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
          placeholder="비밀번호 (8자 이상)"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          minLength={8}
        />
      </div>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <button type="submit">가입하기</button>
      <p>
        이미 계정이 있으신가요? <Link to="/login">로그인</Link>
      </p>
    </form>
  )
}
