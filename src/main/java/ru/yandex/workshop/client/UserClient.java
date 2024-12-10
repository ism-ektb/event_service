package ru.yandex.workshop.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.workshop.dto.UserOutDto;

@FeignClient(name = "user", url = "http://51.250.41.207:8081")
public interface UserClient {
    @RequestMapping(method = RequestMethod.GET, value = "/users/{id}")
    UserOutDto getUser(@RequestHeader Long userId, @RequestHeader String password,@PathVariable("id") long id);
}
