let leagueList = document.getElementById('leagueList');
let teamList = document.getElementById('teamList');
let eplList = document.getElementById('eplList');
let laLigaList = document.getElementById('laLigaList');
let serieAList = document.getElementById('serieAList');
let bundesligaList = document.getElementById('bundesligaList');
let showTypes = false;

document.addEventListener('DOMContentLoaded', function() {
    hideAll();
});

function hideAll() {
    leagueList.style.display = 'none';
    eplList.style.display = 'none';
    laLigaList.style.display = 'none';
    serieAList.style.display = 'none';
    bundesligaList.style.display = 'none';
}

function showTeams(name) {
    if(name == 'EPL') {
        eplList.style.display = 'table-row-group';
        laLigaList.style.display = 'none';
        serieAList.style.display = 'none';
        bundesligaList.style.display = 'none';

    } else if(name == 'LALIGA') {
        laLigaList.style.display = 'table-row-group';
        eplList.style.display = 'none';
        serieAList.style.display = 'none';
        bundesligaList.style.display = 'none';
    } else if(name == 'SERIEA') {
        serieAList.style.display = 'table-row-group';
        eplList.style.display = 'none';
        laLigaList.style.display = 'none';
        bundesligaList.style.display = 'none';
    } else {
        bundesligaList.style.display = 'table-row-group';
        eplList.style.display = 'none';
        laLigaList.style.display = 'none';
        serieAList.style.display = 'none';
    }
}

function showLeagues() {
    if(!showTypes) {
        leagueList.style.display = 'table';
        showTypes = true;
    } else {
        hideAll();
        showTypes = false;
    }
}