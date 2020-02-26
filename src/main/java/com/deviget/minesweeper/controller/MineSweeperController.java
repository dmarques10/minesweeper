package com.deviget.minesweeper.controller;

import javax.validation.Valid;

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
@RequestMapping("/api/v1minesweeper/")
@Validated
@Slf4j
public class MineSweeperController {

	private final GameService gameService;

	@PostMapping(value = "/create", consumes = "application/json")
	public ResponseEntity<GameBean> createGame(@Valid @RequestBody BoardRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createGame(request));
	}

	@PostMapping(value = "/play/{userName}",  consumes = "application/json")
	public ResponseEntity playGame(@Valid @RequestBody PlayRequest request, @PathVariable String userName) {
			return ResponseEntity.ok(gameService.play(userName, request));
	}
}
