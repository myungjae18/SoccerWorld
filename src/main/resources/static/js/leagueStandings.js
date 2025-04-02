let selectedLeague = "PL";
let selectedSeason = "2024";
let isStandings = true;

document.addEventListener('DOMContentLoaded', function () {
    showLeagueRanking();
    createSeasonDropdown();
    applyFilters();
});

function showLeagueRanking() {
    document.getElementById('leagueRanking').style.display = 'block';
    document.getElementById('playerRanking').style.display = 'none';
    document.getElementById('leagueComboBox').style.display = 'block';
    document.getElementById('playerComboBox').style.display = 'none';
    document.getElementById('leagueButton').classList.add('btn-primary');
    document.getElementById('leagueButton').classList.remove('btn-secondary');
    document.getElementById('playerButton').classList.add('btn-secondary');
    document.getElementById('playerButton').classList.remove('btn-primary');
}

function showPlayerRanking() {
    document.getElementById('leagueRanking').style.display = 'none';
    document.getElementById('playerRanking').style.display = 'block';
    document.getElementById('leagueComboBox').style.display = 'none';
    document.getElementById('playerComboBox').style.display = 'block';
    document.getElementById('playerButton').classList.add('btn-primary');
    document.getElementById('playerButton').classList.remove('btn-secondary');
    document.getElementById('leagueButton').classList.add('btn-secondary');
    document.getElementById('leagueButton').classList.remove('btn-primary');
}

function createSeasonDropdown() {
    const seasonDropdown = document.getElementById("seasonDropdown");
    const currentYear = new Date().getFullYear();

    for (let i = 0; i < 5; i++) { // 최근 5개 시즌 생성
        let seasonStart = currentYear - i;
        let seasonEnd = seasonStart + 1;
        let seasonText = `${seasonStart}-${seasonEnd}`;

        let li = document.createElement("li");
        li.innerHTML = `<a class="dropdown-item" href="#" onclick="changeSeason('${seasonStart}')">${seasonText}</a>`;

        seasonDropdown.appendChild(li);
    }
}

function changeSeason(season) {
    selectedSeason = season;
    document.getElementById("dropdownSeasonButton").innerHTML = selectedSeason;
    applyFilters();
}

function changeLeague(league) {
    selectedLeague = league;
    applyFilters();
}

function applyFilters() {
    if(isStandings) {
        fetch(`/standings/${selectedSeason}/${selectedLeague}`)
            .then(response => response.json())
            .then(data => {
                const standings = document.getElementById("standings");
                standings.innerHTML = '';
                data.forEach((item) => {
                    const row = document.createElement(`tr`);

                    row.innerHTML = `
                    <td>${item.position}</td>
                    <td>
                        <img src="${item.teamDto.logo}" width="25px" height="25px"/>
                    </td>
                    <td colspan="10" style="text-align: left;">${item.teamDto.name}</td>
                    <td>${item.playedGames}</td>
                    <td>${item.won}</td>
                    <td>${item.draw}</td>
                    <td>${item.lost}</td>
                    <td>${item.goalDifference}</td>
                    <td>${item.points}</td>
                `;

                    standings.appendChild(row);
                })
            })
            .catch(error => {
                console.error('Error fetching data: ', error);
            });
    } else {
        fetch(`/statistics/${selectedSeason}/${selectedLeague}`)
            .then(response => response.json())
            .then(data => {})
            .catch(error => {
                console.error('Error fetching data: ', error);
            });
    }
}

// function applyFilters() {
//     const rows = ['plTeams', 'laligaTeams', 'serieaTeams', 'bundesligaTeams'];
//
//     rows.forEach(row => {
//         let visible = false;  // 각 row마다 초기화
//
//         // 해당 row의 모든 tr 요소를 가져옴
//         document.querySelectorAll("#" + row + " tr").forEach(rowElement => {
//             const rowLeague = rowElement.getAttribute("league"); // data-league 사용
//             const rowSeason = String(rowElement.getAttribute("season")); // data-season 사용
//
//             // 리그와 시즌이 일치하는 경우 visible을 true로 설정
//             if (selectedLeague === rowLeague && selectedSeason === rowSeason) {
//                 visible = true;
//             }
//
//             rowElement.style.display = (visible) ? "" : "none";
//         });
//     });
// }

// function changeLeague(leagueCode, leagueName) {
//     const leagues = ['eplTeams', 'laligaTeams', 'serieaTeams', 'bundesligaTeams'];
//
//     leagues.splice(leagues.indexOf(leagueCode.toString()), 1); //출력할 랭킹을 목록에서 제거
//
//     leagues.forEach(league => {
//         document.getElementById(league).style.display = "none";
//     });//리스트의 랭킹 가림
//
//     document.getElementById(leagueCode).style.display = "table-row-group";
//
//     document.getElementById('seasonDropdown').innerHTML = leagueName;
// }

// function changePlayerLeague(leagueId, leagueName) {
//     const playerLeagues = ['eplPlayerRanking', 'laligaPlayerRanking', 'serieaPlayerRanking', 'bundesligaPlayerRanking'];
//     playerLeagues.forEach(league => {
//         document.getElementById(league).style.display = "none";
//     });
//     document.getElementById(leagueId).style.display = "table-row-group";
//     document.getElementById('playerDropdownMenuButton').innerHTML = leagueName;
// }