package com.movie.ticket.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DTOConfiguration
{
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }

        @Bean
        public NullAwareBeanUtilsBean NullAware() {
            return new NullAwareBeanUtilsBean();
        }

}
