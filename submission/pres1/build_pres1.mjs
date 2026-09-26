import fs from "node:fs/promises";
import path from "node:path";
import { pathToFileURL } from "node:url";

const root = "E:/Projects/Fitness";
const out = path.join(root, "submission/pres1/IWFC_PRES1_Presentation.pptx");
const build = path.join(root, "target/pres1_build");
const finalizedOut = path.join(build, `IWFC_PRES1_Presentation_${Date.now()}.pptx`);
const skill = "C:/Users/LOQ/.codex/plugins/cache/openai-primary-runtime/presentations/26.909.12148/skills/presentations";
const runtimeModules = process.env.RUNTIME_NODE_MODULES ??
  "C:/Users/LOQ/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules";
const runtimePython = process.env.RUNTIME_PYTHON ??
  "C:/Users/LOQ/.cache/codex-runtimes/codex-primary-runtime/dependencies/python/python.exe";
const { Presentation, PresentationFile } = await import(
  pathToFileURL(path.join(runtimeModules, "@oai/artifact-tool/dist/artifact_tool.mjs")).href
);
const { finalizePresentation } = await import(
  pathToFileURL(path.join(skill, "container_tools/artifact_tool_utils.mjs")).href
);

await fs.mkdir(build, { recursive: true });
await fs.mkdir(path.dirname(out), { recursive: true });
const deck = Presentation.create({ slideSize: { width: 1280, height: 720 } });
const C = { navy:"#16324C", blue:"#2B6F9E", teal:"#167D86", pale:"#EAF2F6",
  ink:"#1C2933", grey:"#5F6C76", white:"#FFFFFF", line:"#C7D5DE", amber:"#A56014" };
const FF = "Arial";

function shape(slide, geometry, x, y, w, h, fill="none", stroke="none", sw=0) {
  return slide.shapes.add({
    geometry, position:{left:x,top:y,width:w,height:h},
    fill, line:{fill:stroke,width:sw}
  });
}
function txt(slide, text, x, y, w, h, size=22, color=C.ink, bold=false, align) {
  const s = shape(slide,"textbox",x,y,w,h);
  s.text = text;
  s.text.style = {typeface:FF,fontSize:size,color,bold,autoFit:"none",alignment:align};
  return s;
}
function line(slide,x1,y1,x2,y2,color=C.line,width=2) {
  return shape(slide,"line",Math.min(x1,x2),Math.min(y1,y2),
    Math.abs(x2-x1),Math.abs(y2-y1),"none",color,width);
}
function base(title, number, notes) {
  const s=deck.slides.add(); s.background.fill=C.white;
  txt(s,title,64,44,1140,66,36,C.navy,true);
  line(s,64,116,1216,116,C.teal,4);
  txt(s,"IWFC  /  CMP 7001 PRES1",64,677,420,22,13,C.grey);
  txt(s,String(number).padStart(2,"0"),1162,675,55,22,13,C.grey,true);
  s.speakerNotes.textFrame.setText(notes);
  return s;
}
function label(s,text,x,y,w=300) { txt(s,text.toUpperCase(),x,y,w,30,16,C.teal,true); }
function block(s,title,body,x,y,w=510) {
  txt(s,title,x,y,w,42,26,C.navy,true);
  txt(s,body,x,y+52,w,110,21,C.ink);
}
function classBox(s,title,x,y,w=180,h=64,detail="") {
  const b=shape(s,"rect",x,y,w,h,C.white,C.navy,1.6);
  txt(s,title,x+8,y+6,w-16,26,title.length>=22?14:17,C.navy,true);
  if(detail) {line(s,x,y+31,x+w,y+31,C.line,1);txt(s,detail,x+8,y+36,w-16,h-38,13,C.grey);}
  return b;
}

// 1
{
  const s=deck.slides.add(); s.background.fill=C.navy;
  txt(s,"Intelligent Wellness and\nFitness Center",74,122,1050,160,57,C.white,true);
  txt(s,"Java prototype  |  CMP 7001 PRES1",76,319,930,42,25,"#D9EDF3");
  line(s,76,398,1185,398,"#68C7C4",4);
  txt(s,"Mohamed Rifad",76,433,620,37,23,C.white);
  txt(s,"Repository: https://github.com/MohamedRifad/intelligent-wellness-and-fitness-center",
    76,503,1120,34,18,C.white);
  txt(s,"Video link: ADD AFTER RECORDING (YouTube or accessible OneDrive)",
    76,562,1120,40,21,"#F5D69A",true);
  s.speakerNotes.textFrame.setText(
    "Timing: 0:00-0:35. Introduce the IWFC prototype and say the presentation will show the design, key code, tests, and running console. The repository is private. Before submission, record the video, upload it to YouTube or an accessible student OneDrive, replace the visible video-link line on this first slide with the real URL, and verify tutor access. Source: CMP 7001 PRES1 brief.");
}
// 2
{
  const s=base("Problem and objective",2,
    "Timing: 0:35-1:15. The scenario starts with fragmented manual equipment records, booking processes, and maintenance reporting. The project objective is one small management tool. Emphasise that this is an in-memory academic prototype, not a deployed production service. Source: CMP 7001 PRAC1 brief.");
  block(s,"Problem","Separate records make resource clashes and fault follow-up hard to control.",75,186,500);
  block(s,"Objective","One use-case boundary connects equipment, sessions, bookings and maintenance.",675,186,500);
  label(s,"prototype scope",75,450);
  txt(s,"15 Java source files     Java 21     Console demo     No database required",75,493,1100,78,27,C.ink,true);
}
// 3
{
  const s=base("Core workflows",3,
    "Timing: 1:15-2:00. Walk through the three actors. An Administrator registers accounts and equipment. An Instructor schedules single or weekly sessions, records usage and reports a fault. A Member books a place and receives booking and affected-session notices. Maintenance progresses Pending to Assigned to Completed. Explain the guardrails: 06:00 to 22:00, overlap checks, capacity and active equipment. Source: PRAC1 brief and IWFCFacade/BookingService/MaintenanceService.");
  const rows=[
    ["Administrator","Registers users; adds and updates equipment; assigns maintenance."],
    ["Instructor","Schedules single or weekly sessions; records usage; reports faults."],
    ["Member","Books an open place and receives relevant notifications."]
  ];
  rows.forEach((r,i)=>{const y=184+i*129;label(s,r[0],76,y,260);txt(s,r[1],324,y-2,810,72,24,C.ink);line(s,76,y+88,1185,y+88,C.line,2);});
  txt(s,"Maintenance status:  PENDING     ASSIGNED     COMPLETED",76,594,1100,41,24,C.navy,true);
}
// 4
{
  const s=base("Implemented class diagram",4,
    "Timing: 2:00-3:15. This is the required class diagram. All 15 top-level production classes appear. Point to the abstract User with three concrete subclasses, then the three core domain entities. The Facade coordinates two services, the Factory and typed GenericRepository. Three checked custom exceptions express failure categories. Nested enums belong to their source files and do not increase the 15-file count. Source: src/main/java, inspected 22 September 2026.");
  // Native, editable UML-style boxes; connectors show generalisation and dependence.
  classBox(s,"User  «abstract»",521,151,238,59,"getRoleDescription()");
  classBox(s,"Administrator",216,245,190,53);
  classBox(s,"Instructor",545,245,190,53);
  classBox(s,"Member",874,245,190,53);
  line(s,311,245,311,220,C.teal,2);line(s,640,245,640,220,C.teal,2);line(s,969,245,969,220,C.teal,2);
  line(s,311,220,969,220,C.teal,2);line(s,640,220,640,210,C.teal,2);
  const y2=337;
  classBox(s,"Equipment",73,y2,190,55,"status; usageHours");
  classBox(s,"FitnessSession",287,y2,190,55,"capacity; bookings");
  classBox(s,"MaintenanceRequest",501,y2,217,55,"PENDING / ASSIGNED");
  classBox(s,"BookingService",743,y2,206,55,"scheduleWeeklySessions");
  classBox(s,"MaintenanceService",973,y2,224,55,"multi-request state");
  classBox(s,"IWFCFacade  «Facade»",399,469,281,62,"viewAllSessions; sample data");
  classBox(s,"EntityFactory  «Factory»",77,562,273,57,"creates users; equipment");
  classBox(s,"GenericRepository<T>",392,562,252,57,"typed in-memory IDs");
  classBox(s,"InvalidBookingException",691,562,230,57);
  classBox(s,"UnauthorizedAccessException",936,562,275,57);
  classBox(s,"DuplicateDataException",699,635,231,49);
  line(s,539,469,539,405,C.teal,2);
  line(s,539,531,539,562,C.teal,2);
  txt(s,"15 top-level production files across 6 packages",74,650,610,30,17,C.grey);
}
// 5
{
  const s=base("Factory, Facade and Observer",5,
    "Timing: 3:15-4:10. Factory creates all three roles and equipment. Facade methods guard role and account state before coordinating weekly scheduling, sample loading and Administrator views. Observer publishing sends fault or 100-hour alerts to active Administrators, assignment and completion to the reporter, and affected-session notices to booked active Members. BookingService uses the same User receiver for confirmations and deterministic wellness tips. Source: EntityFactory, IWFCFacade, BookingService, MaintenanceService, User, tests.");
  block(s,"Factory","EntityFactory creates validated roles and equipment.",74,190,345);
  block(s,"Facade","IWFCFacade checks the actor and coordinates each use case.",468,190,345);
  block(s,"Observer","MaintenanceService routes relevant events to active users.",862,190,340);
  line(s,74,465,1200,465,C.line,2);
  txt(s,"Fault event: Administrators alerted, affected Members warned, reporting Instructor updated",
    74,502,1120,95,23,C.navy,true);
}
// 6
{
  const s=base("Object-oriented design choices",6,
    "Timing: 4:10-4:55. User is abstract and holds shared identity, account state and notification behaviour. Administrator, Instructor and Member override getRoleDescription. A User reference can represent any role, which is real polymorphism. GenericRepository<T> reuses one typed store for four entity types. Collections are chosen by meaning: maps for ID lookup, sets for unique bookings and alerts, lists for ordered snapshots and notifications. Encapsulated status methods stop callers bypassing transitions. Source: domain classes, GenericRepository, evidence pack.");
  label(s,"abstraction and polymorphism",75,176,600);
  txt(s,"User  →  Administrator / Instructor / Member",75,212,1100,60,30,C.navy,true);
  line(s,75,310,1180,310,C.line,2);
  label(s,"generics and collections",75,349,600);
  txt(s,"GenericRepository<T> serves User, Equipment, FitnessSession and MaintenanceRequest.",
    75,390,1100,58,24,C.ink);
  txt(s,"Map: lookup     Set: uniqueness     List: ordered views",75,494,1100,53,25,C.navy,true);
  txt(s,"Private fields and guarded methods protect entity state.",75,580,1100,38,21,C.grey);
}
// 7
{
  const s=base("Validation and exception paths",7,
    "Timing: 4:55-5:40. Explain three checked custom exceptions and the maintenance transition rule. Scheduling rejects outside 06:00-22:00 and overlapping instructor, studio or equipment use. Booking rejects full capacity and repeat Member booking. The Facade rejects wrong roles and inactive or unregistered actors. Duplicate IDs fail in GenericRepository. MaintenanceRequest permits only Pending to Assigned to Completed; invalid transitions use IllegalStateException. Source: BookingService, IWFCFacade, GenericRepository, MaintenanceRequest.");
  const rows=[
    ["InvalidBookingException","Hours, conflicts, unavailable equipment, full or repeat booking"],
    ["UnauthorizedAccessException","Wrong role, inactive or unregistered actor"],
    ["DuplicateDataException","Repeated user, equipment, session or maintenance ID"],
    ["IllegalStateException","Invalid maintenance status transition"]
  ];
  rows.forEach((r,i)=>{const y=175+i*111;txt(s,r[0],75,y,410,49,23,C.navy,true);txt(s,r[1],494,y,700,74,21,C.ink);line(s,75,y+81,1195,y+81,C.line,1);});
}
// 8
{
  const s=base("Verification with 142 passing tests",8,
    "Timing: 5:40-6:30. Maven Surefire recorded 14 test suites, 142 tests, zero failures, zero errors and zero skipped. Coverage includes recurring-series atomicity, booking and schedule notifications, multiple open maintenance requests, sample-data state and console data views. Mention rejected scenarios tested with assertThrows, rather than a permanently failing suite. Boundaries include 06:00 and 22:00, one to twelve recurrence weeks, capacity, and 99.9 versus 100 hours. Source: target/surefire-reports XML and completed codebase.");
  txt(s,"142",77,174,450,177,105,C.teal,true);
  txt(s,"passing JUnit tests",75,347,650,60,34,C.navy,true);
  txt(s,"14 suites   /   0 failures   /   0 errors   /   0 skipped",75,430,1120,54,26,C.ink);
  line(s,75,529,1180,529,C.line,2);
  txt(s,"Boundaries: 06:00 to 22:00     1 to 12 weeks     99.9 / 100 hours",75,565,1120,60,22,C.grey);
}
// 9
{
  const s=base("Integration and defect reflection",9,
    "Timing: 6:30-7:20. The integration suite covers the full workflow through public Facade methods and rejects duplicate equipment, conflicting sessions, unauthorized log access and invalid maintenance transitions. Additional tests prove a four-week atomic series and the three equipment states after one of several requests completes. The preventative-alert design rearms a per-equipment threshold after completion and tests a second cycle without duplicates. Source: IWFCWorkflowIntegrationTest, BookingServiceTest and MaintenanceServiceTest.");
  label(s,"successful path",75,185);
  txt(s,"Register  →  equip  →  schedule  →  book  →  use  →  report  →  assign  →  complete",
    75,227,1120,91,25,C.navy,true);
  line(s,75,357,1180,357,C.line,2);
  label(s,"rejected paths",75,397);
  txt(s,"Duplicate ID   Conflicting session   Unauthorized log   Invalid transition",
    75,440,1110,80,24,C.ink);
  txt(s,"Open requests preserve equipment state; alerts rearm for the next maintenance cycle.",
    75,563,1110,67,22,C.teal,true);
}
// 10
{
  const s=base("Console demonstration",10,
    "Timing: 7:20-8:35. Enter A1 and Rifad. Choose option 2 to load sample data, then option 3 to show S1 at 2/2 and S4-W1 through S4-W4. Choose option 1 to run the guided workflow and point out booking confirmation, wellness tip, schedule notice and the 100-hour preventative alert. Finish with Operational equipment and a Completed request, then select 0. Source: README and IWFCFacade.main.");
  txt(s,"1  Administrator setup: A1 / Rifad",75,178,1020,46,25,C.navy,true);
  txt(s,"2  Load sample data, then view all current data",75,246,1020,46,25,C.navy,true);
  txt(s,"3  Run the guided workflow and show notifications",75,314,1100,72,25,C.navy,true);
  line(s,75,420,1180,420,C.line,2);
  txt(s,"java -cp target\\iwfc-management-system-1.0.0-SNAPSHOT.jar iwfc.app.IWFCFacade",
    75,465,1100,75,20,C.ink);
  txt(s,"Expected finish: equipment OPERATIONAL; request COMPLETED.",75,574,1100,50,22,C.teal,true);
}
// 11
{
  const s=base("Limitations and next steps",11,
    "Timing: 8:35-9:20. Be candid: this is an in-memory, single-process prototype. Restarting loses data. Account identity is guarded by registered object identity and role but has no password authentication. Weekly recurrence supports a fixed interval and occurrence count rather than a general calendar engine. Notifications remain in memory rather than using email or mobile services. Future work is persistent transactional storage, real authentication, concurrency-safe bookings, richer recurrence and durable notification delivery. Source: report and completed codebase.");
  block(s,"Current prototype","In-memory data; guided console; no credential login.",76,183,500);
  block(s,"Known gaps","No database, concurrent transactions or external message delivery.",674,183,500);
  line(s,76,448,1180,448,C.line,2);
  label(s,"future implementation",76,490);
  txt(s,"Persistent storage, transactional booking, authentication and durable notifications",
    76,535,1100,82,26,C.navy,true);
}
// 12
{
  const s=base("What this project demonstrates",12,
    "Timing: 9:20-10:00. Summarise the learning: a small domain model can enforce booking, recurring scheduling and maintenance rules, patterns matter when they support a real workflow, and tests make boundary behaviour explainable. The hardest design choice was retaining 15 production files while adding atomic recurrence and filtered notifications. Show the repository link on slide one again if asked. Finish the recording, replace the video reminder with a verified URL, and check audio and screen capture. Source: completed project and PRES1 brief.");
  txt(s,"A working Java 21 prototype",75,195,1110,70,39,C.navy,true);
  txt(s,"15 production classes   /   three meaningful patterns   /   142 passing tests",
    75,315,1110,80,28,C.teal,true);
  line(s,75,453,1180,453,C.line,2);
  txt(s,"Final manual step: record the 10-minute walkthrough and place its verified link on slide 1.",
    75,507,1100,95,24,C.ink);
}

const staging = path.join(build,"candidate.pptx");
await (await PresentationFile.exportPptx(deck)).save(staging);
const result=await finalizePresentation({
  workspaceDir:root,candidatePath:staging,finalPath:finalizedOut,
  pythonExecutable:runtimePython,
  integrityValidatorPath:path.join(skill,"container_tools/inspect_presentation_package_integrity.py"),
  layoutValidatorPath:path.join(skill,"container_tools/inspect_presentation_layout_geometry.py"),
  layoutArgs:["--expected-slide-size-emu","12192000,6858000","--validate-heading-fit"],
  fontPolicy:{basis:"design",families:[FF]},
  verifyArtifactToolImport:true,
  receiptPath:path.join(build,"IWFC_PRES1_Presentation.validation.json")
});
await fs.copyFile(finalizedOut, out);
console.log(JSON.stringify({output:out,slides:deck.slides.length,result},null,2));
