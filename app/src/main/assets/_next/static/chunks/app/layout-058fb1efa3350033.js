(self.webpackChunk_N_E=self.webpackChunk_N_E||[]).push([[185],{7278:function(e,t,r){Promise.resolve().then(r.bind(r,5865)),Promise.resolve().then(r.t.bind(r,936,23)),Promise.resolve().then(r.t.bind(r,8877,23))},5865:function(e,t,r){"use strict";r.d(t,{default:function(){return p}});var n=r(7437),a=r(1169),i=r(8030);/**
 * @license lucide-react v0.378.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */let o=(0,i.Z)("LayoutDashboard",[["rect",{width:"7",height:"9",x:"3",y:"3",rx:"1",key:"10lvy0"}],["rect",{width:"7",height:"5",x:"14",y:"3",rx:"1",key:"16une8"}],["rect",{width:"7",height:"9",x:"14",y:"12",rx:"1",key:"1hutg5"}],["rect",{width:"7",height:"5",x:"3",y:"16",rx:"1",key:"ldoo1y"}]]);var l=r(4207),s=r(8154),c=r(3622),h=r(1568),d=r(2022);/**
 * @license lucide-react v0.378.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */let u=(0,i.Z)("MessageSquareHeart",[["path",{d:"M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z",key:"1lielz"}],["path",{d:"M14.8 7.5a1.84 1.84 0 0 0-2.6 0l-.2.3-.3-.3a1.84 1.84 0 1 0-2.4 2.8L12 13l2.7-2.7c.9-.9.8-2.1.1-2.8",key:"1blaws"}]]);var f=r(9896),y=r(4839);let m=[{name:"Dashboard",href:"/",icon:o},{name:"Analyze",href:"/analyze",icon:l.Z},{name:"Diet Plan",href:"/diet",icon:s.Z},{name:"Water Tracker",href:"/water",icon:c.Z},{name:"History",href:"/history",icon:h.Z},{name:"Settings",href:"/settings",icon:d.Z},{name:"Feedback",href:"/feedback",icon:u}];function p(){let e=(0,a.usePathname)();if("/login"===e||"/login.html"===e)return null;let t=e=>{"/"===e?window.location.href="/":window.location.href="".concat(e,".html")};return(0,n.jsxs)("aside",{className:"hidden md:flex flex-col w-64 glass-panel border-r border-white/10 z-20 min-h-screen",children:[(0,n.jsx)("div",{className:"p-6",children:(0,n.jsx)("h1",{onClick:()=>t("/"),className:"text-3xl font-extrabold text-gradient-orange tracking-tight cursor-pointer",children:"SymptomSync"})}),(0,n.jsx)("nav",{className:"flex-1 px-4 space-y-2",children:m.map(r=>{let a=e===r.href||e==="".concat(r.href,".html");return(0,n.jsxs)("button",{onClick:()=>t(r.href),className:(0,y.W)("flex items-center gap-3 px-4 py-3.5 w-full text-left rounded-2xl font-bold transition-all cursor-pointer",a?"btn-glow-purple text-white shadow-lg":"text-wellness-white/60 hover:bg-white/5 hover:text-white"),children:[(0,n.jsx)(r.icon,{size:20}),(0,n.jsx)("span",{className:"font-semibold text-base",children:r.name})]},r.name)})}),(0,n.jsx)("div",{className:"p-4 border-t border-white/5",children:(0,n.jsxs)("button",{type:"button",onClick:()=>{localStorage.clear(),sessionStorage.clear(),window.location.href="/login.html"},className:"flex items-center gap-3 px-4 py-3.5 w-full text-wellness-white/70 hover:text-red-400 bg-red-500/10 hover:bg-red-500/20 border border-red-500/20 rounded-2xl transition-all cursor-pointer font-bold shadow-md",children:[(0,n.jsx)(f.Z,{size:20,className:"text-red-400"}),(0,n.jsx)("span",{className:"font-semibold text-base text-red-400",children:"Sign Out"})]})})]})}},8030:function(e,t,r){"use strict";r.d(t,{Z:function(){return s}});var n=r(2265);/**
 * @license lucide-react v0.378.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */let a=e=>e.replace(/([a-z0-9])([A-Z])/g,"$1-$2").toLowerCase(),i=function(){for(var e=arguments.length,t=Array(e),r=0;r<e;r++)t[r]=arguments[r];return t.filter((e,t,r)=>!!e&&r.indexOf(e)===t).join(" ")};/**
 * @license lucide-react v0.378.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */var o={xmlns:"http://www.w3.org/2000/svg",width:24,height:24,viewBox:"0 0 24 24",fill:"none",stroke:"currentColor",strokeWidth:2,strokeLinecap:"round",strokeLinejoin:"round"};/**
 * @license lucide-react v0.378.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */let l=(0,n.forwardRef)((e,t)=>{let{color:r="currentColor",size:a=24,strokeWidth:l=2,absoluteStrokeWidth:s,className:c="",children:h,iconNode:d,...u}=e;return(0,n.createElement)("svg",{ref:t,...o,width:a,height:a,stroke:r,strokeWidth:s?24*Number(l)/Number(a):l,className:i("lucide",c),...u},[...d.map(e=>{let[t,r]=e;return(0,n.createElement)(t,r)}),...Array.isArray(h)?h:[h]])}),s=(e,t)=>{let r=(0,n.forwardRef)((r,o)=>{let{className:s,...c}=r;return(0,n.createElement)(l,{ref:o,iconNode:t,className:i("lucide-".concat(a(e)),s),...c})});return r.displayName="".concat(e),r}},4207:function(e,t,r){"use strict";r.d(t,{Z:function(){return n}});/**
 * @license lucide-react v0.378.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */let n=(0,r(8030).Z)("Activity",[["path",{d:"M22 12h-2.48a2 2 0 0 0-1.93 1.46l-2.35 8.36a.25.25 0 0 1-.48 0L9.24 2.18a.25.25 0 0 0-.48 0l-2.35 8.36A2 2 0 0 1 4.49 12H2",key:"169zse"}]])},3622:function(e,t,r){"use strict";r.d(t,{Z:function(){return n}});/**
 * @license lucide-react v0.378.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */let n=(0,r(8030).Z)("Droplets",[["path",{d:"M7 16.3c2.2 0 4-1.83 4-4.05 0-1.16-.57-2.26-1.71-3.19S7.29 6.75 7 5.3c-.29 1.45-1.14 2.84-2.29 3.76S3 11.1 3 12.25c0 2.22 1.8 4.05 4 4.05z",key:"1ptgy4"}],["path",{d:"M12.56 6.6A10.97 10.97 0 0 0 14 3.02c.5 2.5 2 4.9 4 6.5s3 3.5 3 5.5a6.98 6.98 0 0 1-11.91 4.97",key:"1sl1rz"}]])},1568:function(e,t,r){"use strict";r.d(t,{Z:function(){return n}});/**
 * @license lucide-react v0.378.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */let n=(0,r(8030).Z)("History",[["path",{d:"M3 12a9 9 0 1 0 9-9 9.75 9.75 0 0 0-6.74 2.74L3 8",key:"1357e3"}],["path",{d:"M3 3v5h5",key:"1xhq8a"}],["path",{d:"M12 7v5l4 2",key:"1fdv2h"}]])},9896:function(e,t,r){"use strict";r.d(t,{Z:function(){return n}});/**
 * @license lucide-react v0.378.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */let n=(0,r(8030).Z)("LogOut",[["path",{d:"M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4",key:"1uf3rs"}],["polyline",{points:"16 17 21 12 16 7",key:"1gabdz"}],["line",{x1:"21",x2:"9",y1:"12",y2:"12",key:"1uyos4"}]])},2022:function(e,t,r){"use strict";r.d(t,{Z:function(){return n}});/**
 * @license lucide-react v0.378.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */let n=(0,r(8030).Z)("User",[["path",{d:"M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2",key:"975kel"}],["circle",{cx:"12",cy:"7",r:"4",key:"17ys0d"}]])},8154:function(e,t,r){"use strict";r.d(t,{Z:function(){return n}});/**
 * @license lucide-react v0.378.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */let n=(0,r(8030).Z)("Utensils",[["path",{d:"M3 2v7c0 1.1.9 2 2 2h4a2 2 0 0 0 2-2V2",key:"cjf0a3"}],["path",{d:"M7 2v20",key:"1473qp"}],["path",{d:"M21 15V2v0a5 5 0 0 0-5 5v6c0 1.1.9 2 2 2h3Zm0 0v7",key:"1ogz0v"}]])},8877:function(){},936:function(e){e.exports={style:{fontFamily:"'__Inter_f367f3', '__Inter_Fallback_f367f3'",fontStyle:"normal"},className:"__className_f367f3"}},4839:function(e,t,r){"use strict";function n(){for(var e,t,r=0,n="",a=arguments.length;r<a;r++)(e=arguments[r])&&(t=function e(t){var r,n,a="";if("string"==typeof t||"number"==typeof t)a+=t;else if("object"==typeof t){if(Array.isArray(t)){var i=t.length;for(r=0;r<i;r++)t[r]&&(n=e(t[r]))&&(a&&(a+=" "),a+=n)}else for(n in t)t[n]&&(a&&(a+=" "),a+=n)}return a}(e))&&(n&&(n+=" "),n+=t);return n}r.d(t,{W:function(){return n}})}},function(e){e.O(0,[232,971,23,744],function(){return e(e.s=7278)}),_N_E=e.O()}]);