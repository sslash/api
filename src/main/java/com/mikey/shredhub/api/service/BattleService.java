package com.mikey.shredhub.api.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mikey.shredhub.api.domain.Battle;
import com.mikey.shredhub.api.utils.ImageUploadException;


public interface BattleService {

	Battle getBattleWithId(int battleId);

	List<Battle> getOngoingBattlesForShredderWithId(int id);

	List<Battle> getBattleRequestsForShredderWithId(int id);

	void acceptBattleWithId(int id);

	void addBattleShred(int shredderId, int battleId, String name, int i, MultipartFile shredVideo) throws ImageUploadException;

	void addShredBattleRequest(int shredderId, int shredeeId, String videoName,
			String battleStyle,MultipartFile file) throws ImageUploadException;

	void declineBattleWithId(int id);

}
