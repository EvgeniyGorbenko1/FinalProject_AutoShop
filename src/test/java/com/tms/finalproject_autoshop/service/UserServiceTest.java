//package com.tms.finalproject_autoshop.service;
//
//import com.tms.finalproject_autoshop.model.Security;
//import com.tms.finalproject_autoshop.model.User;
//import com.tms.finalproject_autoshop.model.dto.UserCreateDto;
//import com.tms.finalproject_autoshop.model.dto.UserUpdateDto;
//import com.tms.finalproject_autoshop.repository.SecurityRepository;
//import com.tms.finalproject_autoshop.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.data.domain.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private SecurityRepository securityRepository;
//
//    @InjectMocks
//    private UserService userService;
//
//    private User user;
//    private UserCreateDto createDto;
//    private UserUpdateDto updateDto;
//
//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//
//        user = new User();
//        user.setId(1L);
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setEmail("john@mail.com");
//        user.setAge(30);
//        user.setCreated(LocalDateTime.now());
//        user.setUpdated(LocalDateTime.now());
//
//        createDto = new UserCreateDto();
//        createDto.setFirstName("Alice");
//        createDto.setSecondName("Smith");
//        createDto.setEmail("alice@mail.com");
//        createDto.setAge(25);
//
//        updateDto = new UserUpdateDto();
//        updateDto.setFirstName("Updated");
//        updateDto.setLastName("User");
//        updateDto.setEmail("updated@mail.com");
//    }
//
//    @Test
//    void createUser_success() {
//        when(userRepository.save(any())).thenReturn(user);
//
//        Boolean result = userService.createUser(createDto);
//
//        assertTrue(result);
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//
//    @Test
//    void createUser_exception() {
//        when(userRepository.save(any())).thenThrow(new RuntimeException("DB error"));
//
//        Boolean result = userService.createUser(createDto);
//
//        assertFalse(result);
//    }
//
//    @Test
//    void getUserById_found() {
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        Optional<User> result = userService.getUserById(1L);
//
//        assertTrue(result.isPresent());
//        assertEquals(1L, result.get().getId());
//    }
//
//    @Test
//    void getUserById_notFound() {
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        Optional<User> result = userService.getUserById(1L);
//
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void updateUser_success() {
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        Boolean result = userService.updateUser(updateDto, 1L);
//
//        assertTrue(result);
//        verify(userRepository, times(1)).save(user);
//    }
//
//    @Test
//    void updateUser_notFound() {
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        Boolean result = userService.updateUser(updateDto, 1L);
//
//        assertFalse(result);
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void getAllUsers_success() {
//        when(userRepository.findAll()).thenReturn(List.of(user));
//
//        List<User> result = userService.getAllUsers();
//
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void deleteUser_success() {
//        Security sec = new Security();
//        user.setSecurity(sec);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        Boolean result = userService.deleteUser(1L);
//
//        assertTrue(result);
//        verify(securityRepository, times(1)).delete(sec);
//        verify(userRepository, times(1)).deleteById(1L);
//    }
//
//    @Test
//    void deleteUser_notFound() {
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        Boolean result = userService.deleteUser(1L);
//
//        assertFalse(result);
//    }
//
//    @Test
//    void deleteUser_exception() {
//        when(userRepository.findById(1L)).thenThrow(new RuntimeException("DB error"));
//
//        Boolean result = userService.deleteUser(1L);
//
//        assertFalse(result);
//    }
//
//    @Test
//    void getSortedUsersByField_asc() {
//        when(userRepository.findAll(Sort.by(Sort.Direction.ASC, "firstName")))
//                .thenReturn(List.of(user));
//
//        List<User> result = userService.getSortedUsersByField("firstName", "asc");
//
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void getSortedUsersByField_desc() {
//        when(userRepository.findAll(Sort.by(Sort.Direction.DESC, "firstName")))
//                .thenReturn(List.of(user));
//
//        List<User> result = userService.getSortedUsersByField("firstName", "desk");
//
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void getAllUsersWithPagination_success() {
//        Page<User> page = new PageImpl<>(List.of(user));
//
//        when(userRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
//
//        Page<User> result = userService.getAllUsersWithPagination(0, 10);
//
//        assertEquals(1, result.getContent().size());
//    }
//}
