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

    function copyToClip() {
        let m3u8Src = document.querySelector("#player_one_html5_api > source:nth-child(2)").src;

        let mp4File = "/Users/bennie/Downloads/91/" + document.querySelector("div.videodetails-yakov:nth-child(1) > h4:nth-child(1)").innerText.trim();
        let cmd = `ffmpeg -n -i '${m3u8Src}' -acodec copy -vcodec copy '${mp4File}.mp4'`

        //  let proxy = 'socks5://localhost:1080';
        // cmd = `export all_proxy=${proxy} && ` + cmd;

        var aux = document.createElement("input");
        aux.setAttribute("value", cmd);
        document.body.appendChild(aux);
        aux.select();
        document.execCommand("copy");
        document.body.removeChild(aux);
    }

    var div = document.createElement("div");
    div.setAttribute("onmouseover", "this.style.cursor='pointer'")
    div.appendChild(document.createTextNode("copy cmd"))
    document.querySelector(".like-dislike").parentNode.appendChild(div);

    div.onclick = copyToClip;

})();