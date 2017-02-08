package com.lankr.tv_cloud.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.CharacterEncodingFilter;

public class HtmlFilter extends CharacterEncodingFilter{
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub		
		super.doFilterInternal(request, response, filterChain);
		response.setHeader("Content-Type", "text/html; charset=utf-8");
	}
}
