package com.farmtastic.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmtastic.common.constants.CpnConstants;
import com.farmtastic.member.erum.AuthProvider;
import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemRepository;
import com.farmtastic.memprocpn.model.MemProCpnServiceImp;

@Service
@Transactional  // ← 加這個確保交易提交
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	@Autowired
	private MemRepository memRepository;

	@Autowired
	private MemProCpnServiceImp memProCpnSvc;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oauth2User = super.loadUser(userRequest);
		
        // 處理 Google 登入
//        OAuth2User result = processOAuth2User(oauth2User);
        
        return processOAuth2User(oauth2User);
//		return result;
	}

	private OAuth2User processOAuth2User(OAuth2User oauth2User) {
		String email = oauth2User.getAttribute("email");
		String name = oauth2User.getAttribute("name");
		String googleId = oauth2User.getAttribute("sub");
		
		if (email == null || email.isEmpty()) {
			throw new OAuth2AuthenticationException("無法從 Google 取得 Email");
		}

		Mem mem = memRepository.findByMemEmail(email);
		boolean isNewUser = (mem == null);
		
		System.out.println("是否為新使用者: " + isNewUser);

		if (!isNewUser) {
			// 使用者已存在

			// 檢查是否為傳統方式註冊
			if (mem.getAuthProvider() == AuthProvider.LOCAL) {
				throw new OAuth2AuthenticationException(
					"此 Email 已使用傳統帳密註冊，請使用帳號密碼登入"
				);
			}

			// 更新 Google 使用者資訊
			mem.setMemName(name);
			mem.setProviderId(googleId);
			
		} else {
			// 首次登入 - 自動註冊
			
			System.out.println("建立新使用者");
			
			
			mem = new Mem();
			mem.setMemEmail(email);
			mem.setMemName(name);
			mem.setAuthProvider(AuthProvider.GOOGLE);
			mem.setProviderId(googleId);
			mem.setAccStatus((byte) 1); // Google 登入直接啟用
			
			// 用 email 前綴當帳號（去除特殊字元）
			String autoAccount = email.split("@")[0].replaceAll("[^a-zA-Z0-9]", "");
			if (autoAccount.length() < 8) {
				autoAccount = autoAccount + "google" + System.currentTimeMillis() % 10000;
			}
			mem.setMemAcc(autoAccount);
			
			// password 留空（Google 登入不需要）
		}

		mem = memRepository.save(mem);

		System.out.println("儲存成功，memId=" + mem.getMemId());
		
		// 首次註冊發券
		if (isNewUser) {
			System.out.println("發送新會員優惠券");
			memProCpnSvc.giveCoupon(mem.getMemId(), CpnConstants.REGISTER_DISCOUNT_ID);
			memProCpnSvc.giveCoupon(mem.getMemId(), CpnConstants.REGISTER_CASHBACK_ID);
		}

		return oauth2User;
	}
}