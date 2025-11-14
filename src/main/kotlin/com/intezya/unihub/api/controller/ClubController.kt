package com.intezya.unihub.api.controller

import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.security.RequireUserType
import com.intezya.unihub.service.ClubDto
import com.intezya.unihub.service.ClubService
import com.intezya.unihub.service.StudentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/student/clubs")
@RequireUserType(UserType.STUDENT)
class ClubController(
    private val clubService: ClubService,
    private val studentService: StudentService,
) {

    @GetMapping
    fun getClubs(): List<ClubDto> {
        val studentId = studentService.getCurrentStudentId()
        val studentProfile = studentService.getStudentProfile(studentId)
        return clubService.getClubsForUniversity(studentProfile.universityId)
    }
}
