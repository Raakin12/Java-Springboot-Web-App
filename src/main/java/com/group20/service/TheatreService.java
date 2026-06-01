package com.group20.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.group20.Repository.TheatreRepository;
import com.group20.model.Theatre;

@Service
public class TheatreService {

    private final TheatreRepository theatreRepository;

    public TheatreService(TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
    }

    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }

    public Theatre getTheatreById(Long theatreId) {
        return theatreRepository.findById(theatreId).orElse(null); 
    }

}
