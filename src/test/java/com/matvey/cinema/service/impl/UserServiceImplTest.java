package com.matvey.cinema.service.impl;

import com.matvey.cinema.cache.CacheKeys;
import com.matvey.cinema.cache.InMemoryCache;
import com.matvey.cinema.exception.CustomNotFoundException;
import com.matvey.cinema.model.entities.User;
import com.matvey.cinema.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InMemoryCache cache;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
        user.setEmail("testuser@example.com");
    }

    @Test
    public void testFindById_UserFoundInCache() {
        String cacheKey = CacheKeys.USER_PREFIX + user.getId();
        when(cache.get(cacheKey)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findById(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(cache, times(1)).get(cacheKey);
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    public void testFindById_UserFoundInRepository() {
        when(cache.get(CacheKeys.USER_PREFIX + user.getId())).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findById(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(userRepository, times(1)).findById(user.getId());
        verify(cache, times(1)).put(CacheKeys.USER_PREFIX + user.getId(), user);
    }

    @Test
    public void testFindById_UserNotFound() {
        when(cache.get(CacheKeys.USER_PREFIX + user.getId())).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(CustomNotFoundException.class, () -> userService.findById(user.getId()));
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    public void testFindAll_UsersFoundInCache() {
        String cacheKey = CacheKeys.USERS_ALL;
        when(cache.get(cacheKey)).thenReturn(Optional.of(Collections.singletonList(user)));

        List<User> users = userService.findAll();

        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
        verify(cache, times(1)).get(cacheKey);
        verify(userRepository, never()).findAll();
    }

    @Test
    public void testFindAll_UsersNotFoundInCache() {
        String cacheKey = CacheKeys.USERS_ALL;
        when(cache.get(cacheKey)).thenReturn(Optional.empty());
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> users = userService.findAll();

        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
        verify(userRepository, times(1)).findAll();
        verify(cache, times(1)).put(cacheKey, Collections.singletonList(user));
    }

    @Test
    public void testSave_UserSuccessfullySaved() {
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.save(user);

        assertEquals(user, savedUser);
        verify(userRepository, times(1)).save(user);
        verify(cache, times(1)).evict(CacheKeys.USERS_ALL);
        verify(cache, times(1)).evict(CacheKeys.USER_PREFIX + savedUser.getId());
    }

    @Test
    public void testDeleteById_UserExists() {
        when(userRepository.existsById(user.getId())).thenReturn(true);

        userService.deleteById(user.getId());

        verify(userRepository, times(1)).deleteById(user.getId());
        verify(cache, times(1)).evict(CacheKeys.USERS_ALL);
        verify(cache, times(1)).evict(CacheKeys.USER_PREFIX + user.getId());
    }

    @Test
    public void testDeleteById_UserNotFound() {
        when(userRepository.existsById(user.getId())).thenReturn(false);

        assertThrows(CustomNotFoundException.class, () -> userService.deleteById(user.getId()));
        verify(userRepository, never()).deleteById(user.getId());
    }
}
