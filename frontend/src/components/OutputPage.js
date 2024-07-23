import React from 'react';
import { useLocation } from 'react-router-dom';

const OutputPage = () => {
    const location = useLocation();
    const results = location.state?.results || [];
    return (
        <div className="my-3 mx-4">
            <h1>카페 찾기</h1>
            {Array.isArray(results) && results.length > 0 ? (
                <ul className="my-3">
                    {results.map((result, index) => (
                        <li key={index} className="list-group-item mb-3">
                            <h2 className="h5">{result.cafeName}</h2>
                            <p className="mb-1"><strong>주소 : </strong> {result.cafeAddress}</p>
                            <p className="mb-1"><strong>거리 : </strong> {result.distance}</p>
                            <p className="mb-1">
                                <a href={result.directionUrl} target="_blank" rel="noopener noreferrer" className="btn btn-primary btn-sm mr-2">
                                    길찾기
                                </a>
                                &nbsp;
                                <a href={result.roadViewUrl} target="_blank" rel="noopener noreferrer" className="btn btn-secondary btn-sm">
                                    로드맵
                                </a>
                            </p>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No results found.</p>
            )}
        </div>
    );
}

export default OutputPage;
