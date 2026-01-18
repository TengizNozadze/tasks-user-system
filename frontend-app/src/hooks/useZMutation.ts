import { useState } from 'react'

export default function useZMutation<TRequest = any, TResponse = any>(fn: (req: TRequest) => Promise<TResponse>) {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const mutate = async (payload: TRequest) => {
    setLoading(true)
    setError(null)
    try {
      const res = await fn(payload)
      setLoading(false)
      return res
    } catch (ex: any) {
      setError(ex?.message || 'Unknown error')
      setLoading(false)
      throw ex
    }
  }

  return { mutate, loading, error }
}

