package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.ClubDto
import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.security.RequireUserType
import com.intezya.unihub.service.ClubService
import com.intezya.unihub.service.StudentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/student/clubs")
@RequireUserType(UserType.STUDENT)
class ClubController(
    private val clubService: ClubService,
    private val studentService: StudentService,
) : ClubApi {

    @GetMapping
    override fun getClubs(): List<ClubDto> {
        val studentId = studentService.getCurrentStudentId()
        val studentProfile = studentService.getStudentProfile(studentId)
        return clubService.getClubsForUniversity(studentProfile.universityId)
    }

    @GetMapping("/{id}")
    override fun getClubById(@PathVariable id: Long): ClubDto? {
        val studentId = studentService.getCurrentStudentId()
        val studentProfile = studentService.getStudentProfile(studentId)
        val clubs = clubService.getClubsForUniversity(studentProfile.universityId)
        return clubs.find { it.id == id }
    }
}
