let formEdit=document.getElementById("form-edit");//회원정보 수정 form
let formSt=document.getElementById("form-style");//선호 스타일 변경 form
let nickChecked = false;

//비밀번호 입력 로직
function compare(){
    let newPassword=document.getElementById("new-password");//바꿀 비밀번호 입력
    let password=document.getElementById("password-check");//바꿀 비밀번호 확인

    if(newPassword.value===""){
        alert("비밀번호를 입력하십시오");
    }else if(password.value===""){
        alert("비밀번호 확인을 입력하십시오");
    }else if(password.value !== newPassword.value) {
        alert("비밀번호 두 번 모두 일치하게 입력해주세요");
    } else {
        submitPatch('password');
    }
}

//닉네임 길이 및 중복 체크
function duplicationCheck() {
    let nickname = document.getElementById("nickname").value;

    //닉네임 길이 검사
    if(nickname.length < 2) {
        alert("닉네임을 두 자 이상 입력해주세요");
        return;
    }

    fetch(`/member/check-nickname?nickname=${encodeURIComponent(nickname)}`)
        .then(response => response.json())
        .then(data => {
            nickChecked = data.available;
            if(nickChecked) {
                alert("사용 가능한 닉네임입니다");
            } else {
                alert("다른 닉네임을 사용해주세요");
            }
        })
        .catch(error => {
            console.error(`Error fetching check nickname:`, error);
        });
}

//patch 요청을 모두 받아 보내는 데이터에 맞게 전송
function submitPatch(type) {//type: 수정하려는 데이터의 종류
    let memberId = document.getElementById('member-id');
    let nickname = document.getElementById('nickname');
    let password= document.getElementById("password-check");//바꿀 비밀번호 확인
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const requestData = {};

    if(type=="nickname") {
        if(!nickChecked) {
            alert("닉네임 중복 확인을 먼저 해주세요");
        } else {
            requestData.nickname = nickname.value;
        }
    } else if(type=="password") {
        requestData.password = password.value;
    }

    fetch('/member/'+memberId.value, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken // CSRF 토큰을 헤더에 추가
        },
        body: JSON.stringify(requestData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.text();
    })
    .then(data => {
        console.log('Update successful:', data);
        alert("회원 정보 수정이 완료되었습니다");
        location.href="/member/info?type=info";
    })
    .catch(error => {
        console.error('Error updating nickname:', error);
    });

}

//patch 요청을 모두 받아 보내는 데이터에 맞게 전송
function submitPut(type) {//type: 수정하려는 데이터의 종류
    let memberId = document.getElementById('member-id');
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const requestData = {};

    fetch('/member/'+memberId.value, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken // CSRF 토큰을 헤더에 추가
        },
        body: JSON.stringify(requestData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.text();
        })
        .then(data => {
            console.log('Update successful:', data);
            alert("회원 정보 수정이 완료되었습니다");
            location.href="/member/info?type=info";
        })
        .catch(error => {
            console.error('Error updating nickname:', error);
        });

}