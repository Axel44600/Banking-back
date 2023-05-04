package com.apply.banking.controller;

import com.apply.banking.dto.ContactDto;
import com.apply.banking.service.ContactService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
@Tag(name = "contact")
public class ContactController {

    private final ContactService contactService;

    @PostMapping("/")
    public ResponseEntity<Long> save(@RequestBody ContactDto contactDto) {
        return ResponseEntity.ok(contactService.save(contactDto));
    }

    @GetMapping("/")
    public ResponseEntity<List<ContactDto>> findAll() {
        return ResponseEntity.ok(contactService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(contactService.findById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ContactDto>> findAllByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(contactService.findAllByUserId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        contactService.delete(id);
        return ResponseEntity.accepted().build();
    }
}
