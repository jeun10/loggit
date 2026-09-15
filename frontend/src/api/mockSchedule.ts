export interface DayActivity {
  date: string
  commitCount: number
}

export type TaskCategory = 'NOT_STARTED' | 'IN_PROGRESS' | 'DONE' | 'CANCELED'

export interface TaskItem {
  id: number
  title: string
  dueDate: string
  category: TaskCategory
}

function toISODate(d: Date): string {
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

function addDays(base: Date, days: number): Date {
  const d = new Date(base)
  d.setDate(d.getDate() + days)
  return d
}

const today = new Date()
today.setHours(0, 0, 0, 0)

// 실제 API가 붙기 전까지 쓰는 더미 활동 패턴
const ACTIVITY_PATTERN = [0, 3, 0, 7, 1, 0, 2, 5, 0, 0, 4, 9, 1, 0, 6, 2, 0, 3, 0, 8]

export const activityLog: DayActivity[] = Array.from({ length: 40 }, (_, i) => {
  const date = addDays(today, -i)
  return { date: toISODate(date), commitCount: ACTIVITY_PATTERN[date.getDate() % ACTIVITY_PATTERN.length] }
})

export const tasks: TaskItem[] = [
  { id: 1, title: 'GitHub OAuth 연동', dueDate: toISODate(addDays(today, -3)), category: 'IN_PROGRESS' },
  { id: 2, title: 'V2 마이그레이션 리뷰', dueDate: toISODate(addDays(today, -1)), category: 'NOT_STARTED' },
  { id: 3, title: '메인 캘린더 컴포넌트', dueDate: toISODate(addDays(today, 1)), category: 'IN_PROGRESS' },
  { id: 4, title: '태스크 API 연결', dueDate: toISODate(addDays(today, 4)), category: 'NOT_STARTED' },
  { id: 5, title: '레포 등록 플로우', dueDate: toISODate(addDays(today, 9)), category: 'NOT_STARTED' },
  { id: 6, title: '커밋 동기화 배치', dueDate: toISODate(addDays(today, 20)), category: 'NOT_STARTED' },
]
