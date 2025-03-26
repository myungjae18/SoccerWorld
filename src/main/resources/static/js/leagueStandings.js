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

function changeLeague(leagueCode, leagueName) {
    const leagues = ['eplTeams', 'laligaTeams', 'serieaTeams', 'bundesligaTeams'];

    leagues.splice(leagues.indexOf(leagueCode.toString()), 1); //출력할 랭킹을 목록에서 제거

    leagues.forEach(league => {
        document.getElementById(league).style.display = "none";
    });//리스트의 랭킹 가림

    document.getElementById(leagueCode).style.display = "table-row-group";

    document.getElementById('dropdownMenuButton').innerHTML = leagueName;
}

function changePlayerLeague(leagueId, leagueName) {
    const playerLeagues = ['eplPlayerRanking', 'laligaPlayerRanking', 'serieaPlayerRanking', 'bundesligaPlayerRanking'];
    playerLeagues.forEach(league => {
        document.getElementById(league).style.display = "none";
    });
    document.getElementById(leagueId).style.display = "table-row-group";
    document.getElementById('playerDropdownMenuButton').innerHTML = leagueName;
}

function changeSeason(season) {
    document.getElementById('dropdownSeasonButton').innerHTML = season + ' 시즌';
}

showLeagueRanking();