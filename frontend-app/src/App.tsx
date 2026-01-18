import React, { useState } from 'react'
import { ThemeProvider, createTheme } from '@mui/material/styles'
import { CssBaseline, AppBar, Toolbar, Typography, Button } from '@mui/material'
import UsersPage from './pages/UsersPage'
import TasksPage from './pages/TasksPage'

const theme = createTheme()

export default function App() {
  const [page, setPage] = useState<'users' | 'tasks'>('users')

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flex: 1 }}>Fullstack Platform</Typography>
          <Button color="inherit" onClick={() => setPage('users')}>Users</Button>
          <Button color="inherit" onClick={() => setPage('tasks')}>Tasks</Button>
        </Toolbar>
      </AppBar>
      <main style={{ padding: 16 }}>
        {page === 'users' ? <UsersPage /> : <TasksPage />}
      </main>
    </ThemeProvider>
  )
}

