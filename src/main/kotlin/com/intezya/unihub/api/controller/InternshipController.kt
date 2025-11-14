package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.InternshipDto
import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.security.RequireUserType
import com.intezya.unihub.service.InternshipService
import com.intezya.unihub.service.StudentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/student/internships")
@RequireUserType(UserType.STUDENT, UserType.UNIVERSITY_ADMIN, UserType.ADMIN)
class InternshipController(
    private val internshipService: InternshipService,
    private val studentService: StudentService,
) : InternshipApi {

    @GetMapping
    override fun getInternships(): List<InternshipDto> {
        val studentId = studentService.getCurrentStudentId()
        val studentProfile = studentService.getStudentProfile(studentId)
        return internshipService.getInternshipsForUniversity(studentProfile.universityId)
    }
}
