// ==UserScript==
// @name         91porn utility
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  try to take over the world!
// @author       You
// @match        https://91porn.com/*
// @icon         https://www.google.com/s2/favicons?domain=91porn.com
// @grant        none
// ==/UserScript==

(function () {
    'use strict';

    function copyToClip(event) {
        event.preventDefault();
        event.stopPropagation();

        let m3u8Src = document.querySelector("#player_one_html5_api > source:nth-child(2)").src;

        let mp4File = "~/Downloads/91/" + document.querySelector("div.videodetails-yakov:nth-child(1) > h4:nth-child(1)").innerText.trim();
        let cmd = `mkdir -p ~/Downloads/91/ && ffmpeg -n -i '${m3u8Src}' -acodec copy -vcodec copy '${mp4File}.mp4'`

        // uncomment this if use proxy
        //  let proxy = 'socks5://localhost:1080';
        // cmd = `export all_proxy=${proxy} && ` + cmd;

        var aux = document.createElement("input");
        aux.setAttribute("value", cmd);
        document.body.appendChild(aux);
        aux.select();
        document.execCommand("copy");
        document.body.removeChild(aux);
        btn.textContent = "Copied!";
        setTimeout(() => {
            btn.textContent = "Copy CMD";
        }, 2000);
    }


    var btn = document.createElement("button");
    btn.textContent = "Copy CMD";
    btn.style.cssText = `
        margin-left: 10px;
        padding: 5px 10px;
        background-color: #4CAF50;
        color: white;
        border: none;
        border-radius: 3px;
        cursor: pointer;
        font-size: 12px;
    `;
    btn.onmouseover = function() {
        this.style.backgroundColor = '#45a049';
    };
    btn.onmouseout = function() {
        this.style.backgroundColor = '#4CAF50';
    };
    btn.addEventListener('click', copyToClip, false);
    document.querySelector(".like-dislike").parentNode.appendChild(btn);
})();
