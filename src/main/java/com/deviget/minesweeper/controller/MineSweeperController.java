package com.deviget.minesweeper.controller;

import javax.validation.Valid;
import javax.ws.rs.core.MediaType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deviget.minesweeper.bean.GameBean;
import com.deviget.minesweeper.request.BoardRequest;
import com.deviget.minesweeper.request.PlayRequest;
import com.deviget.minesweeper.service.GameService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/minesweeper/", produces = MediaType.APPLICATION_JSON)
@Validated
@Slf4j
public class MineSweeperController {

	private final GameService gameService;

	@PostMapping(value = "/create", consumes = "application/json", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<GameBean> createGame(@Valid @RequestBody BoardRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createGame(request));
	}

	@PostMapping(value = "/play/{userName}", consumes = "application/json", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity playGame(@Valid @RequestBody PlayRequest request, @PathVariable String userName) {
		return ResponseEntity.ok(gameService.play(userName, request));
	}

	@PostMapping(path = "/{userName}/pause", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity pause(@PathVariable String userName) {
		log.info("Received request to pause game with userName={}", userName);

		gameService.pause(userName);
		return ResponseEntity.ok().build();
	}

	@PostMapping(path = "/{userName}/resume", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity resume(@PathVariable String userName) {
		log.info("Received request to resume game with userName={}", userName);

		gameService.resume(userName);
		return ResponseEntity.ok().build();
	}
}
