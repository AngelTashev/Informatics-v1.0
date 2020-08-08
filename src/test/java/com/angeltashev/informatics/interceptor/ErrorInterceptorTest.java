package com.angeltashev.informatics.interceptor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ErrorInterceptorTest {

    ErrorInterceptor interceptorToTest;

    @Mock
    HttpServletResponse mockResponse;


    @Test
    public void postHandleShouldMapCorrectErrorPage() throws Exception {
        // Arrange
        this.interceptorToTest = new ErrorInterceptor();
        when(mockResponse.getStatus())
                .thenReturn(HttpServletResponse.SC_NOT_FOUND);

        // Act
        this.interceptorToTest.postHandle(Mockito.mock(HttpServletRequest.class),
                mockResponse, Mockito.mock(Object.class), Mockito.mock(ModelAndView.class));

        // Assert
        verify(mockResponse, times(2)).getStatus();
    }
}
