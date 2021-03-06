package aims.user.application;

import aims.user.domain.UserIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import aims.core.infrastructure.exception.Errors.UsrErrCd;
import aims.core.infrastructure.util.CheckUtil;
import aims.user.api.dto.UserRequest;
import aims.user.domain.User;
import aims.user.domain.UserAwareService;

@Service
@Transactional
public class ChangeUserService {
    
    /**
     * 사용자 도메인 서비스
     */
    @Autowired
    private UserAwareService userAwareService;
    
    /**
     * 신규 사용자을 등록한다.
     * 
     * @param request 사용자 요청 데이터
     */
    public void registerUser(UserRequest request) {
        // EBUSRS006 : 등록할 사용자 식별자(%s)가 입력되지 않았습니다.
        CheckUtil.checkArgumentNullOrEmpty(request.getUserId(), UsrErrCd.EBUSRS006.name());
        User user = User.of(new UserIdentity(request.getUserId()), request.getUserNm(), request.getPwd());
        userAwareService.newSave(user);
    }
    
    /**
     * 특정 사용자를 변경한다.
     * 
     * @param userId 사용자 식별자
     * @param request 사용자 요청 데이터
     */
    public void chgUserInfo(String userId, UserRequest request) {
    	// EBUSRS007 : 변경할 사용자 식별자(%s)와 새로운 사용자 식별자(%s)가 입력되지 않았습니다.
        CheckUtil.checkArgumentNullOrEmpty(Lists.newArrayList(userId, request.getUserId()), UsrErrCd.EBUSRS007.name());
        User user = userAwareService.find(userId);
        userAwareService.checkEditDuplicate(userId, request.getUserId());
        
        user.modify(request.getUserNm());
    }
    
    /**
     * 특정 사용자를 삭제한다.
     * 
     * @param userId 사용자 식별자
     */
    public void deleteUser(String userId) {
    	// EBUSRS009 : 삭제할 사용자 식별자(%s)가 입력되지 않았습니다.
        CheckUtil.checkArgumentNullOrEmpty(userId, UsrErrCd.EBUSRS009.name());
        
        User user = userAwareService.find(userId);
        
    	userAwareService.delete(user);
    }

}
