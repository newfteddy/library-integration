package ru.umeta.libraryintegration.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.umeta.libraryintegration.dao.ProtocolDao;
import ru.umeta.libraryintegration.model.Protocol;

@Controller
@RequestMapping("/")
public class HelloController {
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.HEAD})
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "hello";
	}

    @RequestMapping("protocol")
    public Protocol createProtocol(ModelMap model) {
        final ProtocolDao protocolDao = new ProtocolDao();
        final Protocol protocol = new Protocol();
        protocol.setName("protocol1");
        protocolDao.save(protocol);
        return protocol;
    }

}
