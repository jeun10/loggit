import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router'
import { useAuth } from '../api/AuthContext'
import { activityLog, tasks, type TaskItem } from '../api/mockSchedule'
import './MainPage.css'

const WEEKDAYS = ['월', '화', '수', '목', '금', '토', '일']

function dateKey(date: Date) {
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

function startOfDay(date: Date) {
  const copy = new Date(date)
  copy.setHours(0, 0, 0, 0)
  return copy
}

function formatDueDate(value: string) {
  const [, month, day] = value.split('-')
  return `${Number(month)}/${Number(day)}`
}

function CalendarMarks({ count }: { count: number }) {
  if (!count) return <span className="no-activity">—</span>
  return (
    <span className="activity-marks" aria-label={`${count}개의 커밋`}>
      {Array.from({ length: Math.min(count, 5) }, (_, index) => <span key={index} className="activity-mark" />)}
      <span className="activity-count">{count}</span>
    </span>
  )
}

function TaskRow({ task, overdue = false }: { task: TaskItem; overdue?: boolean }) {
  return (
    <li className="task-row">
      <span className={`task-status ${overdue ? 'is-overdue' : ''}`} aria-hidden="true" />
      <div className="task-copy">
        <span className="task-title">{task.title}</span>
        <time dateTime={task.dueDate}>{formatDueDate(task.dueDate)}</time>
      </div>
      <span className={`task-arrow ${overdue ? 'is-overdue' : ''}`} aria-hidden="true">↗</span>
    </li>
  )
}

export function MainPage() {
  const { user, clearAuth } = useAuth()
  const navigate = useNavigate()
  const today = useMemo(() => startOfDay(new Date()), [])
  const [visibleMonth, setVisibleMonth] = useState(() => new Date(today.getFullYear(), today.getMonth(), 1))

  useEffect(() => {
    if (!user) {
      navigate('/login')
    }
  }, [user, navigate])

  const calendarDays = useMemo(() => {
    const year = visibleMonth.getFullYear()
    const month = visibleMonth.getMonth()
    const firstDayOffset = (new Date(year, month, 1).getDay() + 6) % 7
    const daysInMonth = new Date(year, month + 1, 0).getDate()
    return Array.from({ length: firstDayOffset + daysInMonth }, (_, index) =>
      index < firstDayOffset ? null : new Date(year, month, index - firstDayOffset + 1),
    )
  }, [visibleMonth])

  const activityByDate = useMemo(() => new Map(activityLog.map((item) => [item.date, item.commitCount])), [])
  const tasksByDate = useMemo(() => {
    const grouped = new Map<string, TaskItem[]>()
    tasks.filter((task) => task.category !== 'DONE' && task.category !== 'CANCELED').forEach((task) => {
      grouped.set(task.dueDate, [...(grouped.get(task.dueDate) ?? []), task])
    })
    return grouped
  }, [])

  const todayKey = dateKey(today)
  const weekEnd = new Date(today)
  weekEnd.setDate(weekEnd.getDate() + 7)
  const weekEndKey = dateKey(weekEnd)
  const activeTasks = tasks.filter((task) => task.category !== 'DONE' && task.category !== 'CANCELED')
  const overdueTasks = activeTasks.filter((task) => task.dueDate < todayKey)
  const thisWeekTasks = activeTasks.filter((task) => task.dueDate >= todayKey && task.dueDate <= weekEndKey)

  if (!user) return null

  function moveMonth(amount: number) {
    setVisibleMonth((current) => new Date(current.getFullYear(), current.getMonth() + amount, 1))
  }

  return (
    <main className="dashboard-shell">
      <header className="site-header">
        <a className="wordmark" href="/" aria-label="Loggit 홈">Loggit<span>.</span></a>
        <div className="header-actions">
          <span className="user-email">{user.email}</span>
          <button className="logout-button" onClick={() => { clearAuth(); navigate('/login') }}>로그아웃</button>
        </div>
      </header>

      <div className="dashboard-grid">
        <section className="calendar-panel" aria-labelledby="calendar-heading">
          <div className="calendar-header">
            <div><p className="eyebrow">ACTIVITY LOG</p><h1 id="calendar-heading">{visibleMonth.getFullYear()}년 {visibleMonth.getMonth() + 1}월</h1></div>
            <div className="month-controls">
              <button onClick={() => moveMonth(-1)} aria-label="이전 달">←</button>
              <button className="today-button" onClick={() => setVisibleMonth(new Date(today.getFullYear(), today.getMonth(), 1))}>오늘</button>
              <button onClick={() => moveMonth(1)} aria-label="다음 달">→</button>
            </div>
          </div>
          <div className="calendar-grid" role="grid" aria-label={`${visibleMonth.getFullYear()}년 ${visibleMonth.getMonth() + 1}월 활동 달력`}>
            {WEEKDAYS.map((day) => <div className="weekday" role="columnheader" key={day}>{day}</div>)}
            {calendarDays.map((date, index) => {
              if (!date) return <div className="calendar-cell is-empty" key={`empty-${index}`} />
              const key = dateKey(date)
              const isToday = key === todayKey
              const isFuture = date > today
              const dayTasks = tasksByDate.get(key) ?? []
              return (
                <div className={`calendar-cell ${isToday ? 'is-today' : ''} ${isFuture ? 'is-future' : ''}`} role="gridcell" key={key}>
                  <div className="date-line"><time dateTime={key}>{date.getDate()}</time>{isToday && <span className="today-label">오늘</span>}</div>
                  {!isFuture && <CalendarMarks count={activityByDate.get(key) ?? 0} />}
                  {dayTasks.slice(0, 2).map((task) => <span className="calendar-task" key={task.id}>{task.title}</span>)}
                </div>
              )
            })}
          </div>
          <footer className="calendar-legend">
            <span><i className="legend-mark moss" /> 커밋 활동</span><span><i className="legend-mark rust" /> 마감 태스크</span>
            <span className="legend-note">눈금 1개 = 커밋 1개 · 최대 5개 표시</span>
          </footer>
        </section>

        <aside className="task-sidebar" aria-label="할 일 요약">
          <section className="task-section">
            <div className="section-heading"><div><p className="eyebrow rust-text">NEEDS ATTENTION</p><h2>기한 지남</h2></div><span className="section-count is-overdue">{String(overdueTasks.length).padStart(2, '0')}</span></div>
            <ul className="task-list">{overdueTasks.map((task) => <TaskRow task={task} overdue key={task.id} />)}</ul>
          </section>
          <section className="task-section">
            <div className="section-heading"><div><p className="eyebrow">UP NEXT</p><h2>이번 주</h2></div><span className="section-count">{String(thisWeekTasks.length).padStart(2, '0')}</span></div>
            <ul className="task-list">{thisWeekTasks.map((task) => <TaskRow task={task} key={task.id} />)}</ul>
          </section>
          <p className="mock-notice">현재 예시 데이터를 표시하고 있습니다</p>
        </aside>
      </div>
    </main>
  )
}
