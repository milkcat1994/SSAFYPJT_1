import moment from "moment";
require('moment-timezone');
moment.tz.setDefault("Asia/Seoul");
moment.locale('ko');

function getEndDate(startDate, term) {
    return moment(moment(new Date(startDate)).format('YYYY-MM-DD')).add(term, 'day').format('YYYY-MM-DD')
}

function getFormatDate(regtime) {
    return moment(new Date(regtime)).format("YYYY-MM-DD");
}

function getDayDiff(startDate, endDate){
    return moment(new Date(endDate), "YY-MM-DD").diff(moment(new Date(startDate)),'days');
}
export { getEndDate, getFormatDate, getDayDiff };