package com.example.contactapplication.service;

import com.example.contactapplication.entities.Contact;
import com.example.contactapplication.repositories.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {
    @Mock
    private ContactRepository contactRepository;
    @InjectMocks
    private ContactService contactService;

    @Test
    void testGetAllContacts() {
        int page = 0;
        int size = 10;
        contactService.getAllContacts(0, 10);

        verify(contactRepository, times(1))
            .findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    @Test
    void testGetContact() {
        Long id = 1L;
        Contact contact = new Contact();
        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));

        Contact result = contactService.getContact(id);
        assertEquals(contact, result);
        verify(contactRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateContact() {
        Contact contact = new Contact();
        when(contactRepository.save(contact)).thenReturn(contact);

        Contact result = contactService.createContact(contact);
        assertEquals(contact, result);
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void deleteContact() {
        Long id = 1L;
        contactService.deleteContact(id);
        verify(contactRepository, times(1)).deleteById(id);
    }

    @Test
    void testUploadPhoto() {
        Long id = 1L;
        MultipartFile file = new MockMultipartFile("test.jpg", new byte[0]);
        Contact contact = new Contact();

        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));
        when(contactRepository.save(contact)).thenReturn(contact);

        String result = contactService.uploadPhoto(id, file);

        assertNotNull(result);
        verify(contactRepository, times(1)).findById(id);
        verify(contactRepository, times(1)).save(contact);
    }
}