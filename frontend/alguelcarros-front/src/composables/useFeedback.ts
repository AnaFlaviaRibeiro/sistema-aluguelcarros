import { ref } from 'vue'

export type FeedbackKind = 'ok' | 'err'

const feedback = ref<{ kind: FeedbackKind; text: string } | null>(null)

export function useFeedback() {
  function show(kind: FeedbackKind, text: string) {
    feedback.value = { kind, text }
    setTimeout(() => {
      feedback.value = null
    }, 6000)
  }

  return { feedback, show }
}
