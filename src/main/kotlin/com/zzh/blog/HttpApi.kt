package com.zzh.blog

import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException

@RestController
@RequestMapping("/api/article")
class ArticleController(private val repository: ArticleRepository,
                        private val markdownConverter: MarkdownConverter) {

    @GetMapping("/")
    fun findAll() = repository.findAllByOrderByAddedAtDesc()

    @GetMapping("/{id}")
    fun findOne(@PathVariable id: Long, @RequestParam converter: String?) = when (converter) {
        "markdown" -> repository.findById(id).map {
            it.copy(
                    headline = markdownConverter.invoke(it.headline),
                    content = markdownConverter.invoke(it.content)
            )
        }
        null -> repository.findById(id)
        else -> throw IllegalArgumentException("Only markdown converter is supported")
    }
}

@RestController
@RequestMapping("/api/user")
class UserController(private val userRepository: UserRepository) {
    @RequestMapping("/")
    fun findAll() = userRepository.findAll()

    @RequestMapping("/{login}")
    fun findOne(@PathVariable login: String) = userRepository.findById(login)
}