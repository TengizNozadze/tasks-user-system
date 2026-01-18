import React, { useEffect, useState } from 'react'
import { DataGrid, GridColDef } from '@mui/x-data-grid'
import { Button, TextField, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material'
import api from '../api/axiosClient'
import useZMutation from '../hooks/useZMutation'
import { Formik, Form, Field } from 'formik'

type Task = {
  id?: number
  title: string
  category: string
  assignedUsernames?: string[]
  createdAt?: string
}

export default function TasksPage() {
  const [tasks, setTasks] = useState<Task[]>([])
  const [filterCategory, setFilterCategory] = useState('')
  const [filterUser, setFilterUser] = useState('')
  const [openCreate, setOpenCreate] = useState(false)
  const [viewTask, setViewTask] = useState<Task | null>(null)

  const fetch = async () => {
    const res = await api.get<Task[]>('/api/tasks')
    setTasks(res.data || [])
  }

  useEffect(() => { fetch() }, [])

  const { mutate: createTask, loading } = useZMutation((payload: Partial<Task>) => api.post('/api/tasks', payload))

  const handleCreate = async (vals: Partial<Task>) => {
    await createTask(vals)
    setOpenCreate(false)
    fetch()
  }

  const cols: GridColDef[] = [
    { field: 'id', headerName: 'ID', width: 80 },
    { field: 'title', headerName: 'Title', width: 250 },
    { field: 'category', headerName: 'Category', width: 150 },
    { field: 'assignedUsernames', headerName: 'Assigned', width: 300, valueGetter: (p) => (p.row.assignedUsernames || []).join(', ') },
    { field: 'createdAt', headerName: 'Created at', width: 200 }
  ]

  const rows = tasks.filter(t => {
    if (filterCategory && !(t.category || '').toLowerCase().includes(filterCategory.toLowerCase())) return false
    if (filterUser && !((t.assignedUsernames || []).some(u => u.toLowerCase().includes(filterUser.toLowerCase())))) return false
    return true
  })

  return (
    <div>
      <div style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
        <TextField label="Filter by category" value={filterCategory} onChange={e => setFilterCategory(e.target.value)} />
        <TextField label="Filter by username" value={filterUser} onChange={e => setFilterUser(e.target.value)} />
        <Button variant="contained" onClick={() => setOpenCreate(true)}>Create Task</Button>
        <Button variant="outlined" onClick={fetch}>Refresh</Button>
      </div>

      <div style={{ height: 500 }}>
        <DataGrid rows={rows as any} columns={cols} pageSizeOptions={[5, 10]} onRowDoubleClick={(p) => setViewTask(p.row)} />
      </div>

      <Dialog open={openCreate} onClose={() => setOpenCreate(false)}>
        <DialogTitle>Create Task</DialogTitle>
        <DialogContent>
          <Formik initialValues={{ title: '', category: '' }} onSubmit={async (v) => handleCreate(v)}>
            {() => (
              <Form>
                <div style={{ display: 'flex', flexDirection: 'column', gap: 12, width: 400 }}>
                  <Field name="title" as={TextField} label="Title" />
                  <Field name="category" as={TextField} label="Category" />
                  <DialogActions>
                    <Button onClick={() => setOpenCreate(false)}>Cancel</Button>
                    <Button type="submit" variant="contained" disabled={loading}>Create</Button>
                  </DialogActions>
                </div>
              </Form>
            )}
          </Formik>
        </DialogContent>
      </Dialog>

      <Dialog open={!!viewTask} onClose={() => setViewTask(null)} maxWidth="md" fullWidth>
        <DialogTitle>Task details</DialogTitle>
        <DialogContent>
          <pre style={{ whiteSpace: 'pre-wrap' }}>{JSON.stringify(viewTask, null, 2)}</pre>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setViewTask(null)}>Close</Button>
        </DialogActions>
      </Dialog>
    </div>
  )
}

