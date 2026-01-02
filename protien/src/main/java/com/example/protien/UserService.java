package com.example.protien;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CacheManager cacheManager;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.cacheManager = cacheManager;
    }

    // Save user (evict list and by-id caches since we added a new user)
    @CacheEvict(value = {"users", "userById"}, allEntries = true)
    public User saveUser(User user) {
        logger.info("Saving user and evicting cache for email={}", user.getEmail());
        return userRepository.save(user);
    }

    // Check if user already exists (NO caching - used during signup)
    public boolean userExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    // Fetch user for login (NO caching - used during login, password sensitive)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Fetch all users (with caching) - returns wrapper with cache status
    public CacheResult<List<User>> getAllUsersWithCacheStatus() {
        Cache cache = cacheManager.getCache("users");
        String cacheKey = "users::SimpleKey []";
        
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(cacheKey);
            if (wrapper != null) {
                @SuppressWarnings("unchecked")
                List<User> cachedUsers = (List<User>) wrapper.get();
                if (cachedUsers != null) {
                    logger.info("✓ Retrieved all users from Redis cache");
                    return new CacheResult<>(cachedUsers, true);
                }
            }
        }
        
        logger.info("Cache miss - fetching all users from database");
        List<User> users = userRepository.findAll();
        if (cache != null && users != null) {
            cache.put(cacheKey, users);
            logger.info("Cached all users in Redis");
        }
        return new CacheResult<>(users, false);
    }

    // Fetch all users (with caching) - internal method (kept for backward compatibility)
    @Cacheable(value = "users")
    public List<User> getAllUsers() {
        logger.info("Fetching all users from database (will be cached)");
        return userRepository.findAll();
    }

    // Fetch a user by id (with caching) - returns wrapper with cache status
    public CacheResult<User> getUserByIdWithCacheStatus(Long id) {
        Cache cache = cacheManager.getCache("userById");
        String cacheKey = "userById::" + id;
        
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(cacheKey);
            if (wrapper != null) {
                User cachedUser = (User) wrapper.get();
                if (cachedUser != null) {
                    logger.info("✓ Retrieved user id={} from Redis cache", id);
                    return new CacheResult<>(cachedUser, true);
                }
            }
        }
        
        logger.info("Cache miss - fetching user id={} from database", id);
        User user = userRepository.findById(id).orElse(null);
        if (cache != null && user != null) {
            cache.put(cacheKey, user);
            logger.info("Cached user id={} in Redis", id);
        }
        return new CacheResult<>(user, false);
    }

    // Fetch a user by id (with caching) - internal method (kept for backward compatibility)
    @Cacheable(value = "userById", key = "#id")
    public User getUserById(Long id) {
        logger.info("Fetching user id={} from database (will be cached)", id);
        return userRepository.findById(id).orElse(null);
    }

    // Wrapper class to return data with cache status
    public static class CacheResult<T> {
        private final T data;
        private final boolean fromCache;

        public CacheResult(T data, boolean fromCache) {
            this.data = data;
            this.fromCache = fromCache;
        }

        public T getData() {
            return data;
        }

        public boolean isFromCache() {
            return fromCache;
        }
    }
}
