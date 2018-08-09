package com.zzh.blog

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class HtmlController(private val articleRepository: ArticleRepository,
                     private val markdownConverter: MarkdownConverter) {
    @GetMapping("/")
    fun blog(model: Model): String {
        model["title"] = "blog"
        model["articles"] = articleRepository.findAllByOrderByAddedAtDesc().map { it.render() }
        return "blog"
    }

    @GetMapping("/article/{id}")
    fun article(@PathVariable id: Long, model: Model): String {
        val article = articleRepository
                .findById(id)
                .orElseThrow { IllegalArgumentException("Wrong article id provided") }
                .render()
        model["title"] = article.title
        model["article"] = article
        return "article"
    }

    private fun Article.render() = RenderedArticle(
            title,
//            markdownConverter.invoke(headline),
//            markdownConverter.invoke(content),
            headline,
            content,
            author,
            id,
            addedAt.format())

    data class RenderedArticle(
            val title: String,
            val headline: String,
            val content: String,
            val author: User?,
            val id: Long?,
            val addedAt: String)
}


