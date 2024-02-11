package com.fufa.fariska.entities;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.Subject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

@SuperBuilder
@Data
public class User implements Principal {
    private int id;
    private String gmail;
    private String nickname;
    public ModelAndView modelAndView;


    @Override
    public String getName() {
//        httpServletRequest.getLocalAddr();
        return gmail;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
