package kr.boot.basic.servlet.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "requestGetServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[전체 파라미터 조회] - start");
         /*
         Enumeration<String> parameterNames = request.getParameterNames();
         while (parameterNames.hasMoreElements()) {
         String paramName = parameterNames.nextElement();
         System.out.println(paramName + "=" +
        request.getParameter(paramName));
         }
         */
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> System.out.println(paramName +
                        "=" + request.getParameter(paramName)));
        System.out.println("[전체 파라미터 조회] - end");
        System.out.println();
        System.out.println("[단일 파라미터 조회]");
        String name = request.getParameter("name");
        System.out.println("request.getParameter(name) = " + name);
        String age = request.getParameter("age");
        System.out.println("request.getParameter(age) = " + age);
        System.out.println();
        // http://localhost:8081/request-get?name=hello&name=world
        // 중복으로 보내는 경우는 check box 같을 때
        System.out.println("[이름이 같은 복수 파라미터 조회]");
        System.out.println("request.getParameterValues(name)");
        String[] names = request.getParameterValues("name");
        if (names != null) {
            for (String n : names) {
                System.out.println("name=" + n);
            }
        }
        response.getWriter().write("ok");
    }
}
