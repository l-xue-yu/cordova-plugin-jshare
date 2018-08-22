var exec = require("cordova/exec");

exports.Platform = {
  Wechat: "Wechat",
  WechatMoments: "WechatMoments",
  WechatFavorite: "WechatFavorite",
  SinaWeibo: "SinaWeibo",
  SinaWeiboMessage: "SinaWeiboMessage",
  QQ: "QQ",
  QZone: "QZone",
  Facebook: "Facebook",
  FbMessenger: "FbMessenger",
  Twitter: "Twitter"
};

exports.Type = {
  Text: 1,
  Image: 2,
  WebPage: 3,
  Music: 4,
  Video: 5,
  Apps: 6,
  File: 7
};

exports.Params = function() {
  this.text;
  this.video_path;
  this.image_path;
  this.image_data;
  this.file_path;
  this.title;
  this.image_url;
  this.comment;
  this.url;
  this.music_url;
  this.quote;
};

exports.setDebugMode = function(mode, success, error) {
  exec(success, error, "JShare", "setDebugMode", [mode]);
};

exports.init = function(success, error) {
  exec(success, error, "JShare", "init");
};

exports.getPlatformList = function(success, error) {
  exec(success, error, "JShare", "getPlatformList");
};

exports.isClientValid = function(platform, success, error) {
  exec(success, error, "JShare", "isClientValid", [platform]);
};

exports.share = function(platform, type, params, success, error) {
  exec(success, error, "JShare", "share", [platform, type, params]);
};

exports.isSupportAuthorize = function(platform, success, error) {
  exec(success, error, "JShare", "isSupportAuthorize", [platform]);
};

exports.isAuthorize = function(platform, success, error) {
  exec(success, error, "JShare", "isAuthorize", [platform]);
};

exports.removeAuthorize = function(platform, success, error) {
  exec(success, error, "JShare", "removeAuthorize", [platform]);
};

exports.authorize = function(platform, success, error) {
  exec(success, error, "JShare", "authorize", [platform]);
};

exports.getUserInfo = function(platform, success, error) {
  exec(success, error, "JShare", "getUserInfo", [platform]);
};
