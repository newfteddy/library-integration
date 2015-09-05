package ru.umeta.libraryintegration.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by ctash on 9/5/2015.
 */
Controller
RequestMapping("/kt")
public class KotlinController {

    @RequestMapping(method = arrayOf(RequestMethod.GET, RequestMethod.POST))
    fun handlePost(request: HttpServletRequest, response: HttpServletResponse): String {
        return "kthello";
    }

}
