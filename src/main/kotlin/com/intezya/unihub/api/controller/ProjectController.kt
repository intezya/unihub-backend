package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.ProjectDto
import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.security.RequireUserType
import com.intezya.unihub.service.ProjectService
import com.intezya.unihub.service.StudentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/student/projects")
@RequireUserType(UserType.STUDENT, UserType.UNIVERSITY_ADMIN, UserType.ADMIN)
class ProjectController(
    private val projectService: ProjectService,
    private val studentService: StudentService,
) : ProjectApi {

    @GetMapping
    override fun getProjects(): List<ProjectDto> {
        val studentId = studentService.getCurrentStudentId()
        val studentProfile = studentService.getStudentProfile(studentId)
        return projectService.getProjectsForUniversity(studentProfile.universityId)
    }

    @GetMapping("/{id}")
    override fun getProjectById(@PathVariable id: Long): ProjectDto? {
        val studentId = studentService.getCurrentStudentId()
        val studentProfile = studentService.getStudentProfile(studentId)
        val projects = projectService.getProjectsForUniversity(studentProfile.universityId)
        return projects.find { it.id == id }
    }
}
