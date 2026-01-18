import React, { useEffect, useState } from 'react'
import { DataGrid, GridColDef } from '@mui/x-data-grid'
import { Button, TextField, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material'
import api from '../api/axiosClient'
import useZMutation from '../hooks/useZMutation'
import { Formik, Form, Field } from 'formik'

type User = {
  id?: number
  username: string
  fullName?: string
  category?: string
}

export default function UsersPage() {
  const [users, setUsers] = useState<User[]>([])
  const [filter, setFilter] = useState('')
  const [openCreate, setOpenCreate] = useState(false)
  const [viewUser, setViewUser] = useState<User | null>(null)

  const fetch = async () => {
    const res = await api.get<User[]>('/api/users')
    setUsers(res.data || [])
  }

  useEffect(() => { fetch() }, [])

  const { mutate: createUser, loading } = useZMutation((payload: User) => api.post('/api/users', payload))

  const handleCreate = async (vals: User) => {
    await createUser(vals)
    setOpenCreate(false)
    fetch()
  }

  const cols: GridColDef[] = [
    { field: 'id', headerName: 'ID', width: 80 },
    { field: 'username', headerName: 'Username', width: 200 },
    { field: 'fullName', headerName: 'Full name', width: 200 },
    { field: 'category', headerName: 'Category', width: 150 }
  ]

  const rows = users.filter(u => !filter || (u.category || '').toLowerCase().includes(filter.toLowerCase()))

  return (
    <div>
      <div style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
        <TextField label="Filter by category" value={filter} onChange={e => setFilter(e.target.value)} />
        <Button variant="contained" onClick={() => setOpenCreate(true)}>Create User</Button>
        <Button variant="outlined" onClick={fetch}>Refresh</Button>
      </div>

      <div style={{ height: 420 }}>
        <DataGrid rows={rows as any} columns={cols} pageSizeOptions={[5, 10]} onRowDoubleClick={(p) => setViewUser(p.row)} />
      </div>

      <Dialog open={openCreate} onClose={() => setOpenCreate(false)}>
        <DialogTitle>Create User</DialogTitle>
        <DialogContent>
          <Formik initialValues={{ username: '', fullName: '', category: '' }} onSubmit={async (v) => handleCreate(v)}>
            {() => (
              <Form>
                <div style={{ display: 'flex', flexDirection: 'column', gap: 12, width: 400 }}>
                  <Field name="username" as={TextField} label="Username" />
                  <Field name="fullName" as={TextField} label="Full name" />
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

      <Dialog open={!!viewUser} onClose={() => setViewUser(null)}>
        <DialogTitle>User details</DialogTitle>
        <DialogContent>
          <pre style={{ whiteSpace: 'pre-wrap' }}>{JSON.stringify(viewUser, null, 2)}</pre>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setViewUser(null)}>Close</Button>
        </DialogActions>
      </Dialog>
    </div>
  )
}

