package ru.le.link.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.le.link.model.Link;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class MainController {
    private static List<Link> links = Stream.of(
            new Link("title1", "content1"),
            new Link("title2", "content2"),
            new Link("title3", "content3")
    ).collect(Collectors.toList());

    @RequestMapping("/")
    public String main() {
        return "main";
    }

    @RequestMapping("/link")
    @ResponseBody
    public List<Link> getAllLinks() {
        return links;
    }

    @MessageMapping("/changeLink")
    @SendTo("/topic/activity")
    public Link change(Link link) {
        links.add(link);
        return link;
    }
}
