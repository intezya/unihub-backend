package com.intezya.unihub.service

import com.intezya.unihub.domain.repository.LessonRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class StudentService(
    private val studentRepository: StudentRepository,
    private val lessonRepository: LessonRepository,
) {
    fun getNextLessonForStudent(studentId: UUID): Lesson? {
        val student = studentRepository.findById(studentId).orElse(null) ?: return null
        val schedule = student.schedule ?: return null
        return lessonRepository.findNextLessonForSchedule(schedule.id!!, LocalDateTime.now())
    }
}
