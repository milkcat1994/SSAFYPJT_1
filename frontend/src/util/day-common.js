import moment from "moment";
require('moment-timezone');
moment.tz.setDefault("Asia/Seoul");
moment.locale('ko');

function getEndDate(startDate, term) {
    return moment(moment.utc(new Date(startDate)).format('YYYY-MM-DD')).add(term, 'day').format('YYYY-MM-DD')
}

function getFormatDate(regtime) {
    return moment.utc(new Date(regtime)).format("YYYY-MM-DD");
}

function getDayDiff(startDate, endDate){
    return moment.utc(new Date(endDate), "YY-MM-DD").diff(moment.utc(new Date(startDate)),'days');
}
export { getEndDate, getFormatDate, getDayDiff };