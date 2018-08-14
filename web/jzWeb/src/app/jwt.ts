export class MyJwt {

  public static get USERPASS_PREFIX(): string { return "UP_"; };
  public static get VK_PREFIX(): string { return "VK_"; };
  public static get OK_PREFIX(): string { return "OK_"; };

  public static getUsernameFromJwt(token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace('-', '+').replace('_', '/');
    //let jwtObj = JSON.parse(window.atob(base64));
    let jwtObj = JSON.parse(MyJwt.b64DecodeUnicode(base64));
    var username = jwtObj["sub"];
    username = decodeURI(username);
    console.log("MyJwt username=" + username);
    if (username && username.startsWith(MyJwt.USERPASS_PREFIX)) {
      username = username.substring(MyJwt.USERPASS_PREFIX.length);
    }
    if (username && username.startsWith(MyJwt.VK_PREFIX)) {
      username = jwtObj["name"];
      username = decodeURI(username);
      username = username.replace('+', ' ');
    }
    if (username && username.startsWith(MyJwt.OK_PREFIX)) {
      username = jwtObj["name"];
      username = decodeURI(username);
      username = username.replace('+', ' ');
    }
    return username;
  };

  private static b64DecodeUnicode(str) {
    return decodeURIComponent(Array.prototype.map.call(atob(str), function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
    }).join(''))
  }

}