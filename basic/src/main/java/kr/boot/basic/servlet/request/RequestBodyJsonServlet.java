package kr.boot.basic.servlet.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.boot.basic.domain.Person;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "requestBodyJsonServlet", urlPatterns = "/request-bodyjson")
public class RequestBodyJsonServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse
            response)
            throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream,
                StandardCharsets.UTF_8);
        System.out.println("messageBody = " + messageBody);

        // 이렇게 하면 json 데이터를 자바 객체로 받아서 읽을 수 있음
        Person yeonmi = objectMapper.readValue(messageBody, Person.class);
        System.out.println("yeonmi.getName() = " + yeonmi.getName());
        System.out.println("yeonmi.getAge() = " + yeonmi.getAge());

        response.getWriter().write("ok");
    }
}
