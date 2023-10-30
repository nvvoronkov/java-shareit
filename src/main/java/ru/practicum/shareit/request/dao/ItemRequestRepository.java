package ru.practicum.shareit.request.dao;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequester(User requester);

    Optional<ItemRequest> findById(Long id);

    List<ItemRequest> findAllByRequesterNot(User requester, PageRequest pageable);
}
