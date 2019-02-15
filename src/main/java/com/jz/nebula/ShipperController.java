package com.jz.nebula;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jz.nebula.entity.Shipper;
import com.jz.nebula.repository.ShipperRepository;

@Controller
@RequestMapping("/shippers")
public class ShipperController {
    
	@Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private ShipperRepository shipperRepository;
    
    @RequestMapping("/{id}")
    public String greeting(@PathVariable("id") long id, Model model) {
    	Optional<Shipper> shipper = shipperRepository.findById(id);
    	
    	model.addAttribute("name", shipper.get().getName());
        return "shipper/index";
    }
}
