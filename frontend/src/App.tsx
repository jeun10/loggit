import { Route, Routes } from 'react-router'
import { AuthProvider } from './api/AuthContext'
import { LoginPage } from './pages/LoginPage'
import { MainPage } from './pages/MainPage'
import { SignupPage } from './pages/SignupPage'

function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/" element={<MainPage />} />
      </Routes>
    </AuthProvider>
  )
}

export default App
